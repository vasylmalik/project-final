package com.lazareff.taskmanager.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequest {

    @NotBlank(message = "Refresh token cannot be blank")
    private String refreshToken;
}
