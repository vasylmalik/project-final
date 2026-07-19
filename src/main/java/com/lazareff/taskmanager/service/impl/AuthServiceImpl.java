package com.lazareff.taskmanager.service.impl;

import com.lazareff.taskmanager.dto.auth.LoginRequest;
import com.lazareff.taskmanager.dto.auth.LoginResponse;
import com.lazareff.taskmanager.dto.auth.RefreshTokenRequest;
import com.lazareff.taskmanager.dto.auth.RegisterRequest;
import com.lazareff.taskmanager.entity.User;
import com.lazareff.taskmanager.enums.RoleType;
import com.lazareff.taskmanager.exception.*;
import com.lazareff.taskmanager.repository.UserRepository;
import com.lazareff.taskmanager.security.jwt.JwtService;
import com.lazareff.taskmanager.service.AuthService;
import com.lazareff.taskmanager.service.RoleService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.lazareff.taskmanager.entity.RefreshToken;
import com.lazareff.taskmanager.repository.RefreshTokenRepository;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void register(RegisterRequest request) {

        log.info("Registration attempt for email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {

            log.warn("Registration failed. Email already exists: {}", request.getEmail());

            throw new EmailAlreadyExistsException(
                    "Email " + request.getEmail() + " already exists"
            );
        }

        User user = new User();

        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEnabled(true);

        user.setRole(
                roleService.findByRole(RoleType.USER)
        );

        userRepository.save(user);

        log.info("User registered successfully. Id: {}, Email: {}",
                user.getId(),
                user.getEmail());

    }

    @Override
    public LoginResponse login(LoginRequest request) {

        log.info("Login attempt for email: {}", request.getEmail());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(
                        "User with email " + request.getEmail() + " not found"
                ));

        String accessToken = jwtService.generateAccessToken(user.getEmail());

        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        RefreshToken refreshTokenEntity = new RefreshToken();

        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setUser(user);

        refreshTokenEntity.setExpiryDate(
                jwtService.getRefreshTokenExpiryDate()
        );

        refreshTokenRepository.save(refreshTokenEntity);

        log.info("User logged in successfully. Id: {}, Email: {}",
                user.getId(),
                user.getEmail());

        LoginResponse response = new LoginResponse();

        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);

        return response;
    }

    @Override
    @Transactional
    public LoginResponse refreshToken(RefreshTokenRequest request) {

        log.info("Refresh token request received.");

        if (!jwtService.isTokenValid(request.getRefreshToken())) {

            log.warn("Refresh token validation failed.");

            throw new RefreshTokenExpiredException(
                    "Refresh token is invalid"
            );
        }

        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(request.getRefreshToken())
                .orElseThrow(() ->
                        new RefreshTokenNotFoundException(
                                "Refresh token not found"
                        ));

        if (refreshToken.isRevoked()) {

            log.warn("Attempt to use revoked refresh token.");

            throw new RefreshTokenRevokedException(
                    "Refresh token revoked"
            );
        }

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {

            log.warn("Attempt to use expired refresh token.");

            throw new RefreshTokenExpiredException(
                    "Refresh token expired"
            );
        }

        User user = refreshToken.getUser();
        String email = user.getEmail();

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        String newRefreshToken =
                jwtService.generateRefreshToken(email);

        RefreshToken newToken = new RefreshToken();

        newToken.setToken(newRefreshToken);
        newToken.setUser(user);
        newToken.setExpiryDate(
                jwtService.getRefreshTokenExpiryDate()
        );

        refreshTokenRepository.save(newToken);

        log.info("Refresh token successfully renewed for user: {}",
                email);

        String accessToken =
                jwtService.generateAccessToken(email);

        LoginResponse response = new LoginResponse();

        response.setAccessToken(accessToken);
        response.setRefreshToken(newRefreshToken);

        return response;
    }

    @Override
    @Transactional
    public void logout(String refreshToken) {

        log.info("Logout request received.");

        RefreshToken token = refreshTokenRepository
                .findByToken(refreshToken)
                .orElseThrow(() ->
                        new RefreshTokenNotFoundException(
                                "Refresh token not found"
                        ));

        token.setRevoked(true);

        refreshTokenRepository.save(token);

        log.info("User logged out successfully. User id: {}",
                token.getUser().getId());
    }

}
