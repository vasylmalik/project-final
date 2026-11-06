package com.javarush.jira.mail;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.javarush.jira.common.internal.config.AppConfig;
import com.javarush.jira.common.internal.config.AppProperties;
import com.javarush.jira.common.util.Util;
import com.javarush.jira.login.User;
import com.javarush.jira.mail.internal.MailCase;
import com.javarush.jira.mail.internal.MailCaseRepository;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.*;
import java.util.concurrent.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    private static final Locale LOCALE_RU = Locale.forLanguageTag("ru");
    private static final String OK = "OK";

    private final MailCaseRepository mailCaseRepository;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    private final AppConfig appConfig;
    private final AppProperties appProperties;

    //    https://stackoverflow.com/a/50287955/548473
    @Qualifier("mailExecutor")
    private final Executor mailExecutor;

    @Value("${spring.mail.username}")
    private String email;

    public static boolean isOk(String result) {
        return OK.equals(result);
    }

    public String sendToUserWithParams(@NonNull String template, @NonNull User user, @NonNull Map<String, Object> params) {
        String email = Objects.requireNonNull(user.getEmail());
        Map<String, Object> extParams = Util.mergeMap(params, Map.of("user", user));
        return send(appConfig.isProd() ? email : appProperties.getTestMail(), user.getFirstName(), template, extParams);
    }

    public String send(String toEmail, String toName, String template, Map<String, Object> params) {
        log.debug("Send email to {}, {} with template {}", toEmail, toName, template);
        String result = OK;
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
            message.setFrom(email, "JiraRush");
            String content = getContent(template, params);
            message.setText(content, true);
            message.setSubject(Util.getTitle(content));  // TODO calculate title for group emailing only once
            message.setTo(new InternetAddress(toEmail, toName, "UTF-8"));
            if (!appConfig.isTest()) {
                javaMailSender.send(mimeMessage);
            }
        } catch (Exception e) {
            result = e.getMessage();
            log.error("Sending to {} failed: \n{}", toEmail, result);
            mailCaseRepository.save(new MailCase(toEmail, toName, template, result));
        }
        return result;
    }

    private String getContent(String template, Map<String, Object> params) {
        Context context = new Context(LOCALE_RU, params);
        return templateEngine.process(template, context);
    }

    public synchronized GroupResult sendToGroup(@NonNull String template, @NonNull Set<User> users, Map<String, Object> params) {
        if (users.isEmpty()) {
            return new GroupResult(0, Collections.emptyList(), null);
        }
        CompletionService<String> completionService = new ExecutorCompletionService<>(mailExecutor);
        Map<Future<String>, String> resultMap = new HashMap<>();
        users.forEach(
                user -> {
                    Future<String> future = completionService.submit(() -> sendToUserWithParams(template, user, params));
                    resultMap.put(future, user.getEmail());
                }
        );

        GroupResultBuilder groupResultBuilder = new GroupResultBuilder();
        try {
            while (!resultMap.isEmpty()) {
                Future<String> future = completionService.poll(10, TimeUnit.SECONDS);
                if (future == null) {
                    cancelAll(resultMap);
                    return groupResultBuilder.buildWithFailure("+++ Interrupted by timeout");
                } else {
                    String email = resultMap.remove(future);
                    if (!groupResultBuilder.accept(email, future)) {
                        cancelAll(resultMap);
                        return groupResultBuilder.buildWithFailure("+++ Interrupted by faults number");
                    }
                }
            }
        } catch (InterruptedException e) {
            return groupResultBuilder.buildWithFailure("+++ Interrupted");
        }
        return groupResultBuilder.buildOK();
    }

    @Async("mailExecutor")
    public void sendToUserAsync(@NonNull String template, @NonNull User user, Map<String, Object> params) {
        sendToUserWithParams(template, user, params);
    }

    private void cancelAll(Map<Future<String>, String> resultMap) {
        log.warn("Cancel all un-sent emails");
        resultMap.forEach((future, email) -> {
            log.warn("Sending to " + email + " failed");
            future.cancel(true);
        });
    }

    public static class MailResult {
        private final String email;
        private final String result;

        @JsonCreator
        MailResult(@JsonProperty("email") String email, @JsonProperty("result") String result) {
            this.email = email;
            this.result = result;
        }

        @Override
        public String toString() {
            return '(' + email + ',' + result + ')';
        }
    }

    private static class GroupResultBuilder {
        private final List<MailResult> failed = new ArrayList<>();
        private int success = 0;
        private String failedCause = null;

        private GroupResult buildOK() {
            return new GroupResult(success, failed, null);
        }

        private GroupResult buildWithFailure(String cause) {
            return new GroupResult(success, failed, cause);
        }

        private boolean accept(String email, Future<String> future) {
            try {
                String result = future.get();
                if (isOk(result)) {
                    success++;
                } else {
                    failed.add(new MailResult(email, result));
                }
            } catch (InterruptedException e) {
                failedCause = "Task interrupted";
                log.error("Sending to " + email + " interrupted");
            } catch (ExecutionException e) {
                failed.add(new MailResult(email, e.toString()));
                log.error("Sending to " + email + " failed with " + e.getMessage());
            }
            int failedSize = failed.size();
            return ((failedSize < 6 || (double) failedSize / (success + failedSize) < 0.15)) && failedCause == null;
        }
    }

    public static class GroupResult {
        private final int success;
        private final List<MailResult> failed;
        private final String failedCause;

        public GroupResult(int success, List<MailResult> failed, String failedCause) {
            this.success = success;
            this.failed = List.copyOf(failed);
            this.failedCause = failedCause;
            log.info(toString());
        }

        @Override
        public String toString() {
            return "Success: " + success + '\n' +
                    "Failed: " + failed.toString() + '\n' +
                    (failedCause == null ? "" : "Failed cause" + failedCause);
        }
    }
}
