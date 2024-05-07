package com.javarush.jira.common.error;

public class RefreshTokenException extends RuntimeException {
    public RefreshTokenException(String format) {
        super(format);
    }
}
