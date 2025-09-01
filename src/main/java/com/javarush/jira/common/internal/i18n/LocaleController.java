package com.javarush.jira.common.internal.i18n;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Locale;

@Controller
public class LocaleController {

    @GetMapping("/i18n/set")
    public String setLang(@RequestParam("lang") String lang,
                          HttpServletRequest request,
                          HttpServletResponse response) {
        LocaleResolver resolver = RequestContextUtils.getLocaleResolver(request);
        if (resolver != null) {
            Locale target = Locale.forLanguageTag(lang);
            resolver.setLocale(request, response, target);
        }
        return "redirect:/view/login";
    }
}
