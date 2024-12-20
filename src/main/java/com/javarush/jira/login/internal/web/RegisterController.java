package com.javarush.jira.login.internal.web;

import com.javarush.jira.common.error.DataConflictException;
import com.javarush.jira.common.util.validation.View;
import com.javarush.jira.login.UserDTO;
import com.javarush.jira.login.internal.verification.ConfirmData;
import com.javarush.jira.login.internal.verification.RegistrationConfirmEvent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import static com.javarush.jira.common.util.validation.ValidationUtil.checkNew;

@Controller
@RequestMapping(RegisterController.REGISTER_URL)
@RequiredArgsConstructor
@Slf4j
public class RegisterController extends AbstractUserController {
    static final String REGISTER_URL = "/ui/register";

    private final ApplicationEventPublisher eventPublisher;

    @GetMapping
    public String register(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "unauth/register";
    }

    @PostMapping
    public String register(@Validated(View.OnCreate.class) UserDTO userDTO, BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()) {
            return "unauth/register";
        }
        log.info("register {}", userDTO);
        checkNew(userDTO);
        ConfirmData confirmData = new ConfirmData(userDTO);
        request.getSession().setAttribute("token", confirmData);
        eventPublisher.publishEvent(new RegistrationConfirmEvent(userDTO, confirmData.getToken()));
        return "redirect:/view/login";
    }

    @GetMapping("/confirm")
    public String confirmRegistration(@RequestParam String token, SessionStatus status, HttpSession session,
                                      @SessionAttribute("token") ConfirmData confirmData) {
        log.info("confirm registration {}", confirmData);
        if (token.equals(confirmData.getToken())) {
            handler.createFromDTO(confirmData.getUserDTO());
            session.invalidate();
            status.setComplete();
            return "login";
        }
        throw new DataConflictException("Token mismatch error");
    }
}
