package com.javarush.jira.login.internal.web;

import com.javarush.jira.common.error.IllegalRequestDataException;
import com.javarush.jira.login.User;
import com.javarush.jira.login.internal.UserHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import static com.javarush.jira.common.internal.config.SecurityConfig.PASSWORD_ENCODER;

@Slf4j
public abstract class AbstractUserController {
    @Autowired
    protected UserHandler handler;
    @Autowired
    private UniqueMailValidator emailValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        Object target = binder.getTarget();
        if (target != null && emailValidator.supports(target.getClass())) {
            binder.addValidators(emailValidator);
        }
    }

    public void changePassword0(String oldPassword, String newPassword, long id) {
        log.info("change password for user with id={}", id);
        User user = handler.getRepository().getExisted(id);
        if (!PASSWORD_ENCODER.matches(oldPassword, user.getPassword())) {
            throw new IllegalRequestDataException("Wrong old password");
        }
        user.setPassword(PASSWORD_ENCODER.encode(newPassword));
        handler.getRepository().save(user);
    }
}
