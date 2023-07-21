package com.javarush.jira.common.internal.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@ConfigurationProperties("app")
@Validated
@Getter
@Setter
public class AppProperties {

    /**
     * Host url
     */
    @NonNull
    private String hostUrl;

    /**
     * Test email
     */
    @NonNull
    private String testMail;

    /**
     * Interval for update templates
     */
    @NonNull
    private Duration templatesUpdateCache;

    @NonNull
    private MailSendingProps mailSendingProps;

    //    https://stackoverflow.com/a/29588215/548473
    @Setter
    public static class MailSendingProps {
        int corePoolSize;
        int maxPoolSize;
    }
}
