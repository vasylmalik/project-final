package com.lazareff.taskmanager.service;

import com.lazareff.taskmanager.dto.auth.LoginRequest;
import com.lazareff.taskmanager.dto.auth.LoginResponse;
import com.lazareff.taskmanager.dto.auth.RefreshTokenRequest;
import com.lazareff.taskmanager.dto.auth.RegisterRequest;

public interface AuthService {

    void register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    LoginResponse refreshToken(RefreshTokenRequest request);

    void logout(String refreshToken);

}
