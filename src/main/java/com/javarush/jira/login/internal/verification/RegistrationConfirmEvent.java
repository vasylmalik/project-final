package com.javarush.jira.login.internal.verification;

import com.javarush.jira.common.AppEvent;
import com.javarush.jira.login.UserTo;
import jakarta.servlet.http.HttpServletRequest;

public record RegistrationConfirmEvent(UserTo userto, String token) implements AppEvent {
}
