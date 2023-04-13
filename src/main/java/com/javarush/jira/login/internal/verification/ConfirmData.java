package com.javarush.jira.login.internal.verification;

import com.javarush.jira.login.UserTo;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

@Getter
@ToString
public class ConfirmData implements Serializable {
    private final UserTo userTo;
    private final String token;

    public ConfirmData(@NonNull UserTo user) {
        this.userTo = user;
        this.token = UUID.randomUUID().toString();
    }
}
