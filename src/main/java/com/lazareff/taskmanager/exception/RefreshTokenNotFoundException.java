package com.lazareff.taskmanager.exception;

public class RefreshTokenNotFoundException extends RuntimeException {

    public RefreshTokenNotFoundException(String message) {
        super(message);
    }

}
