package com.javarush.jira.common.error;

import org.springframework.lang.NonNull;

import java.io.IOException;

public class AppException extends RuntimeException {

    public AppException(@NonNull String message) {
        super(message);
    }

    public AppException(String message, IOException e) {
        super(message, e);
    }
}
