package com.javarush.jira.common.internal.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LocaleController {

    @GetMapping("/ui/lang")
    public String changeLocale(@RequestParam("lang") String lang, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }
}
