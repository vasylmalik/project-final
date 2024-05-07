package com.javarush.jira.login.internal.web;

import com.javarush.jira.common.internal.config.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class AuthController {

    private final AuthService authService;

    @PostMapping("/view/login")
    public String login(@RequestBody LoginRequestDto loginRequestDto) {

        String username = loginRequestDto.username();
        String password = loginRequestDto.password();

        authService.authenticate(username, password);

        HttpHeaders headers = new HttpHeaders();
        authService.accessToken(username, headers);
        authService.refreshToken(username, headers);

        return "index";
    }
}
