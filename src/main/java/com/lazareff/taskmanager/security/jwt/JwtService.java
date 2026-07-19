package com.lazareff.taskmanager.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import io.jsonwebtoken.Jwts;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    public String generateAccessToken(String email) {

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(email)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + accessTokenExpiration
                        )
                )
                .signWith(getSigningKey())
                .compact();

    }

    public String generateRefreshToken(String email) {

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(email)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + refreshTokenExpiration
                        )
                )
                .signWith(getSigningKey())
                .compact();

    }

    public String extractEmail(String token) {

        return extractAllClaims(token).getSubject();

    }

    public boolean isTokenValid(String token) {

        try {

            extractAllClaims(token);
            return true;

        } catch (Exception ex) {

            return false;

        }

    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );

    }

    public Instant getRefreshTokenExpiryDate() {

        return Instant.now().plusMillis(refreshTokenExpiration);

    }
}
