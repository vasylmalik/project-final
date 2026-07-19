package com.lazareff.taskmanager.controller;

import com.lazareff.taskmanager.dto.auth.LoginRequest;
import com.lazareff.taskmanager.dto.auth.LoginResponse;
import com.lazareff.taskmanager.dto.auth.RefreshTokenRequest;
import com.lazareff.taskmanager.dto.auth.RegisterRequest;
import com.lazareff.taskmanager.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {

        return authService.login(request);
    }

    @PostMapping("/register")
    public void register(@Valid @RequestBody RegisterRequest request) {

        authService.register(request);

    }

    @PostMapping("/logout")
    public void logout(
            @Valid @RequestBody RefreshTokenRequest request) {

        authService.logout(request.getRefreshToken());

    }

    @PostMapping("/refresh")
    public LoginResponse refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {

        return authService.refreshToken(request);

    }
}
