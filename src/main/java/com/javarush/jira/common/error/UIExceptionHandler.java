package com.javarush.jira.common.error;

import com.javarush.jira.login.AuthUser;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@ControllerAdvice
@Slf4j
public class UIExceptionHandler extends BasicExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ModelAndView bindException(BindException ex, WebRequest request) {
        return processException(ex, request,
                (e, type, msg, r) -> {
                    String collectedMsg = String.join("<br>", errorMessageHandler.getErrorList(ex.getBindingResult()));
                    return getExceptionView(e, type, collectedMsg, request);
                });
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView exception(Exception ex, WebRequest request) {
        return processException(ex, request, UIExceptionHandler::getExceptionView);
    }

    // Error from ErrorController
    ModelAndView processError(@Nullable Throwable th, String msg, String path, Integer status) {
        if (th instanceof Exception ex) {
            return processException(ex, null, path, UIExceptionHandler::getExceptionView);
        } else {
            log.error(ERR_PFX + "Exception " + msg + " at request " + path);
            return getExceptionView(th, ErrorType.APP_ERROR, msg, null);
        }
    }

    private static ModelAndView getExceptionView(@Nullable Throwable ex, ErrorType type, String msg, @Nullable WebRequest request) {
        return extendModelAndView(type.status, new ModelAndView("exception", Map.of("type", type, "msg", msg)));
    }

    private static ModelAndView extendModelAndView(HttpStatus status, ModelAndView modelAndView) {
        modelAndView.getModelMap().addAttribute("authUser", AuthUser.safeGet());
        modelAndView.setStatus(status);
        return modelAndView;
    }
}