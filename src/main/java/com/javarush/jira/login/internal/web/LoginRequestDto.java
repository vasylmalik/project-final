package com.javarush.jira.login.internal.web;

public record LoginRequestDto(
        String username,
        String password
) {
}
