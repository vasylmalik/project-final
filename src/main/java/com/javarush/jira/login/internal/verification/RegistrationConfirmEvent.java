package com.javarush.jira.login.internal.verification;

import com.javarush.jira.common.AppEvent;
import com.javarush.jira.login.UserTo;

public record RegistrationConfirmEvent(UserTo userto, String token) implements AppEvent {
}
