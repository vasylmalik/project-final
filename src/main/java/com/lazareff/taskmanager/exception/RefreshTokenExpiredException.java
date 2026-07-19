package com.lazareff.taskmanager.exception;

public class RefreshTokenExpiredException extends RuntimeException {

    public RefreshTokenExpiredException(String message) {
        super(message);
    }

}
