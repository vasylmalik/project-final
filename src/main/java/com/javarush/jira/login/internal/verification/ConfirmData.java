package com.javarush.jira.login.internal.verification;

import com.javarush.jira.login.UserDTO;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

@Getter
@ToString
public class ConfirmData implements Serializable {
    private final UserDTO userDTO;
    private final String token;

    public ConfirmData(@NonNull UserDTO user) {
        this.userDTO = user;
        this.token = UUID.randomUUID().toString();
    }
}
