package com.javarush.jira.common.internal.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import java.util.Locale;

@Controller
public class LanguageController {

    private final LocaleResolver localeResolver;

    public LanguageController(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    @GetMapping("/change-language")
    public String changeLanguage(@RequestParam("lang") String langCode,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        Locale locale = new Locale(langCode);
        localeResolver.setLocale(request, response, locale);
        return "redirect:" + request.getHeader("Referer");
    }
}
