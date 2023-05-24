package com.javarush.jira.common.error;

import org.springframework.lang.NonNull;

public class WrongTagException extends RuntimeException{
    public WrongTagException(@NonNull String message) {
        super(message);
    }
}
