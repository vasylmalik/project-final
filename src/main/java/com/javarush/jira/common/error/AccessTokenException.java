package com.javarush.jira.common.error;

public class AccessTokenException extends RuntimeException {
    public AccessTokenException(String format) {
        super(format);
    }
}
