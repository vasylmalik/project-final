package com.javarush.jira.common.internal.web;

import com.javarush.jira.common.error.ErrorType;
import com.javarush.jira.login.AuthUser;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Map;

@ControllerAdvice(annotations = Controller.class)
@Slf4j
public class UIExceptionHandler extends BasicExceptionHandler {

    private static ModelAndView getExceptionView(@Nullable Throwable ex, ErrorType type, String msg, @Nullable HttpServletRequest request) {
        return extendModelAndView(type.status,
                (ex != null && ex.getClass() == NoHandlerFoundException.class) ?
                        new ModelAndView("404") :
                        new ModelAndView("exception", Map.of("type", type, "msg", msg))
        );
    }

    private static ModelAndView extendModelAndView(HttpStatus status, ModelAndView modelAndView) {
        modelAndView.getModelMap().addAttribute("authUser", AuthUser.safeGet());
        modelAndView.setStatus(status);
        return modelAndView;
    }

    @ExceptionHandler(BindException.class)
    public ModelAndView bindException(BindException ex, HttpServletRequest request) {
        return processException(ex, request,
                (e, type, msg, r) -> {
                    String collectedMsg = String.join("<br>", errorMessageHandler.getErrorList(ex.getBindingResult()));
                    return getExceptionView(e, type, collectedMsg, request);
                });
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView exception(Exception ex, HttpServletRequest request) {
        return processException(ex, request, UIExceptionHandler::getExceptionView);
    }

    // Error from ErrorController
    ModelAndView processError(@Nullable Throwable th, String msg, String path, Integer status) {
        if (th instanceof Exception ex) {
            return processException(ex, null, path, UIExceptionHandler::getExceptionView);
        }

        log.error(ERR_PFX + "Exception " + msg + " at request " + path);
        return getExceptionView(th, ErrorType.APP_ERROR, msg, null);
    }
}