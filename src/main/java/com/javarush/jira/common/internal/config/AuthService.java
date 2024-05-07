package com.javarush.jira.common.internal.config;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public void authenticate(String username, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
    }

    public void accessToken(String email, HttpHeaders headers) {
        String token = jwtService.generateToken(email);
        headers.set("Authorization", "Bearer " + token);
    }

    public void refreshToken(String email, HttpHeaders headers) {
        refreshTokenService.deleteByUserEmail(email);
        String token = refreshTokenService.generateToken(email);
        addRTokenToCookies(token, headers);
    }

    public void addRTokenToCookies(String refreshToken, HttpHeaders headers) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(-1)
                .build();

        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void deleteCookie(String cookieName, HttpHeaders headers) {
        ResponseCookie cookie = ResponseCookie.from(cookieName, "")
                .maxAge(0)
                .build();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void deleteHeader(String header, HttpHeaders headers) {
        headers.set(header, "");
    }
}
