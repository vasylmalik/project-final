package com.javarush.jira.common.internal.config;

import com.javarush.jira.common.error.RefreshTokenException;
import com.javarush.jira.common.model.RefreshToken;
import com.javarush.jira.login.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Value("${jwt.refresh.duration}")
    private Duration duration;

    @Transactional
    public String generateToken(String email) {
        User user = userService.findByEmailIgnoreCase(email);

        String token = UUID.randomUUID().toString();

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(token)
                .expiryDate(LocalDateTime.now().plus(duration.toMillis(), ChronoUnit.MILLIS))
                .build();

        return refreshTokenRepository.save(refreshToken).getToken();
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenException("Token not found."));
    }

    @Transactional
    public void deleteByUserEmail(String email) {
        refreshTokenRepository.deleteByUserEmail(email);
    }

    public String findEmailByToken(String token) {
        return refreshTokenRepository.findEmailByToken(token);
    }
}
