package com.lazareff.taskmanager.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {

    private String accessToken;

    private String refreshToken;

    private String tokenType = "Bearer";
}
