package com.lazareff.taskmanager.exception;

public class RefreshTokenRevokedException extends RuntimeException {

    public RefreshTokenRevokedException(String message) {
        super(message);
    }

}
