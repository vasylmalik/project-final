package com.javarush.jira.login.internal.web;

import com.javarush.jira.common.error.ErrorMessageHandler;
import com.javarush.jira.common.error.IllegalRequestDataException;
import com.javarush.jira.login.AuthUser;
import com.javarush.jira.login.UserTo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(UserUIController.USER_URL)
@RequiredArgsConstructor
public class UserUIController extends AbstractUserController {
    public static final String USER_URL = "/ui/user";
    private static final String REDIRECT_PROFILE = "/ui/profile";
    private static final String USER_ERROR_ATTRIBUTE = "userError";
    private static final String USER_SUCCESS_ATTRIBUTE = "userSuccess";

    private final ErrorMessageHandler errorMessageHandler;

    @PostMapping
    public String update(@Valid UserTo user, BindingResult result, @AuthenticationPrincipal AuthUser authUser,
                         RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            redirectAttrs.addFlashAttribute(USER_ERROR_ATTRIBUTE, errorMessageHandler.getErrorList(result));
            return "redirect:" + REDIRECT_PROFILE;
        }
        authUser.setUser(handler.updateFromTo(user, authUser.id()));
        redirectAttrs.addFlashAttribute(USER_SUCCESS_ATTRIBUTE, "Successfully changed");
        return "redirect:" + REDIRECT_PROFILE;
    }

    @PostMapping("/change-password")
    public String changePassword(@Valid PasswordChange passwordChange, BindingResult result,
                                 @AuthenticationPrincipal AuthUser authUser, RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            redirectAttrs.addFlashAttribute(USER_ERROR_ATTRIBUTE, errorMessageHandler.getErrorList(result));
            return "redirect:" + REDIRECT_PROFILE;
        }
        try {
            changePassword0(passwordChange.oldPassword, passwordChange.newPassword, authUser.id());
        } catch (IllegalRequestDataException e) {
            redirectAttrs.addFlashAttribute(USER_ERROR_ATTRIBUTE, e.getMessage());
            return "redirect:" + REDIRECT_PROFILE;
        }
        redirectAttrs.addFlashAttribute(USER_SUCCESS_ATTRIBUTE, "Password successfully changed");
        return "redirect:" + REDIRECT_PROFILE;
    }

    public record PasswordChange(String oldPassword, @Size(min = 5, max = 128) String newPassword) {
    }
}
