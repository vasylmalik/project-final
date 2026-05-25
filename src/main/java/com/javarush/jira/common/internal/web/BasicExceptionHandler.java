package com.javarush.jira.common.internal.web;

import com.javarush.jira.common.error.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.FileNotFoundException;
import java.nio.file.AccessDeniedException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static com.javarush.jira.common.error.ErrorType.APP_ERROR;

@Slf4j
public class BasicExceptionHandler {
    public static final String ERR_PFX = "ERR# ";
    //    https://stackoverflow.com/a/52254601/548473
    static final Map<Class<? extends Throwable>, ErrorType> HTTP_STATUS_MAP = new LinkedHashMap<>() {
        {
// more specific first
            put(NotFoundException.class, ErrorType.NOT_FOUND);
            put(NoHandlerFoundException.class, ErrorType.NOT_FOUND);
            put(DataConflictException.class, ErrorType.DATA_CONFLICT);
            put(IllegalRequestDataException.class, ErrorType.BAD_REQUEST);
            put(AppException.class, ErrorType.APP_ERROR);
            put(EntityNotFoundException.class, ErrorType.DATA_CONFLICT);
            put(DataIntegrityViolationException.class, ErrorType.DATA_CONFLICT);
            put(IllegalArgumentException.class, ErrorType.BAD_DATA);
            put(BindException.class, ErrorType.BAD_REQUEST);
            put(ValidationException.class, ErrorType.BAD_REQUEST);
            put(HttpRequestMethodNotSupportedException.class, ErrorType.BAD_REQUEST);
            put(MissingServletRequestParameterException.class, ErrorType.BAD_REQUEST);
            put(RequestRejectedException.class, ErrorType.BAD_REQUEST);
            put(FileNotFoundException.class, ErrorType.BAD_REQUEST);
            put(AccessDeniedException.class, ErrorType.FORBIDDEN);
            put(AuthenticationException.class, ErrorType.UNAUTHORIZED);
        }
    };
    @Autowired
    protected ErrorMessageHandler errorMessageHandler;

    //  https://stackoverflow.com/a/65442410/548473
    @NonNull
    public static Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }

    protected <T> T processException(@NonNull Exception ex, HttpServletRequest request, Processor<T> processor) {
        return processException(ex, request, request.getRequestURI(), processor);
    }

    protected <T> T processException(@NonNull Exception ex, @Nullable HttpServletRequest request, String path, Processor<T> processor) {
        Class<? extends Exception> exClass = ex.getClass();
        Optional<ErrorType> type = HTTP_STATUS_MAP.entrySet().stream()
                .filter(
                        entry -> entry.getKey().isAssignableFrom(exClass)
                )
                .findAny().map(Map.Entry::getValue);
        if (type.isPresent()) {
            log.error(ERR_PFX + "Exception {} at request {}", ex, path);
            return processor.process(ex, type.get(), ex.getMessage(), request);
        } else {
            Throwable root = getRootCause(ex);
            log.error(ERR_PFX + "Exception " + root + " at request " + path, root);
            return processor.process(ex, APP_ERROR, "Exception " + root.getClass().getName(), request);
        }
    }

    interface Processor<T> {
        T process(Exception ex, ErrorType type, String msg, HttpServletRequest request);
    }
}
