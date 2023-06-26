package com.javarush.jira.login.internal.web;

import com.javarush.jira.common.error.DataConflictException;
import com.javarush.jira.common.internal.config.SecurityConfig;
import com.javarush.jira.login.User;
import com.javarush.jira.login.internal.UserRepository;
import com.javarush.jira.login.internal.passwordreset.PasswordResetEvent;
import com.javarush.jira.login.internal.passwordreset.ResetData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Slf4j
@Controller
@RequestMapping(PasswordController.PASSWORD_URL)
@RequiredArgsConstructor
public class PasswordController {
    static final String PASSWORD_URL = "/ui/password";
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @PostMapping("/reset")
    public String resetPassword(@RequestParam String email, HttpServletRequest request) {
        log.info("reset password {}", email);
        User user = userRepository.getExistedByEmail(email);
        ResetData resetData = new ResetData(email);
        request.getSession().setAttribute("token", resetData);
        eventPublisher.publishEvent(new PasswordResetEvent(user, resetData.getToken()));
        return "redirect:/view/login";
    }

    @GetMapping("/change")
    public String changePassword(@RequestParam String token, Model model,
                                 @SessionAttribute(name = "token") ResetData resetData) {
        log.info("change password {}", resetData);
        if (token.equals(resetData.getToken())) {
            model.addAttribute("token", token);
            return "/unauth/change-password";
        }
        throw new DataConflictException("Token mismatch error");
    }

    @Transactional
    @PostMapping("/save")
    @CacheEvict(value = "users", key = "#resetData.email")
    public String savePassword(@RequestParam String token, @RequestParam String password,
                               @SessionAttribute(name = "token") ResetData resetData,
                               SessionStatus status, HttpSession session) {
        log.info("save password {}", resetData);
        if (token.equals(resetData.getToken())) {
            User user = userRepository.getExistedByEmail(resetData.getEmail());
            String encodedPassword = SecurityConfig.PASSWORD_ENCODER.encode(password);
            user.setPassword(encodedPassword);
            session.invalidate();
            status.setComplete();
        }
        return "redirect:/view/login";
    }
}
