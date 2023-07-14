/*
 * Copyright 2015 Stormpath, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.javarush.jira.common.internal.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.*;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    private static final String ERROR_PATH = "/error";

    private final ErrorAttributes errorAttributes;

    private final UIExceptionHandler uiExceptionHandler;

    @RequestMapping(ERROR_PATH)
    public ModelAndView error(WebRequest request) {
        Map<String, Object> errorAttributesMap = this.errorAttributes.getErrorAttributes(request, ErrorAttributeOptions.of(MESSAGE, EXCEPTION, BINDING_ERRORS));
        Throwable error = errorAttributes.getError(request);
        String path = (String) errorAttributesMap.get("path");
        Integer status = (Integer) errorAttributesMap.get("status");
        String msg = (String) errorAttributesMap.get("message");
        return uiExceptionHandler.processError(error, msg, path, status);
    }
}
