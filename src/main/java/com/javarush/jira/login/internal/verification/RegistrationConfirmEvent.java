package com.javarush.jira.login.internal.verification;

import com.javarush.jira.common.AppEvent;
import com.javarush.jira.login.UserDTO;

public record RegistrationConfirmEvent(UserDTO userDTO, String token) implements AppEvent {
}
