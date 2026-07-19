package com.lazareff.taskmanager.exception;

import com.lazareff.taskmanager.dto.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                message,
                request
        );
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoleNotFound(
            RoleNotFoundException ex,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(
            UserNotFoundException ex,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaskNotFound(
            TaskNotFoundException ex,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCommentNotFound(
            CommentNotFoundException ex,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(
            EmailAlreadyExistsException ex,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request
        );
    }


    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRefreshTokenNotFound(
            RefreshTokenNotFoundException ex,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleRefreshTokenExpired(
            RefreshTokenExpiredException ex,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(RefreshTokenRevokedException.class)
    public ResponseEntity<ErrorResponse> handleRefreshTokenRevoked(
            RefreshTokenRevokedException ex,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception ex,
            HttpServletRequest request) {

        log.error("Unhandled exception. URI: {}",
                request.getRequestURI(),
                ex);

        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error",
                request
        );
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );

        return ResponseEntity
                .status(status)
                .body(error);
    }

}
