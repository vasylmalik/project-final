package com.javarush.jira.common.error;

import org.springframework.http.HttpStatus;

public enum ErrorType {
    APP_ERROR("Application error", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_DATA("Wrong data", HttpStatus.UNPROCESSABLE_ENTITY),
    BAD_REQUEST("Bad request", HttpStatus.UNPROCESSABLE_ENTITY),
    DATA_CONFLICT("DB data conflict", HttpStatus.CONFLICT),
    NOT_FOUND("Wrong data in request", HttpStatus.NOT_FOUND),
    AUTH_ERROR("Authorization error", HttpStatus.FORBIDDEN),
    UNAUTHORIZED("Request unauthorized", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("Request forbidden", HttpStatus.FORBIDDEN);

    public final String title;
    public final HttpStatus status;
    ErrorType(String title, HttpStatus status) {
        this.title = title;
        this.status = status;
    }
}
