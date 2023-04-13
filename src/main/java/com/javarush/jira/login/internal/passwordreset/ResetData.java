package com.javarush.jira.login.internal.passwordreset;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

@Getter
@ToString
public class ResetData implements Serializable {
    private final String token;
    private final String email;

    public ResetData(@NotNull String email) {
        this.token = UUID.randomUUID().toString();
        this.email = email;
    }
}
