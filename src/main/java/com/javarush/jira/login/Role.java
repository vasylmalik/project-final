package com.javarush.jira.login;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    DEV,
    ADMIN;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}