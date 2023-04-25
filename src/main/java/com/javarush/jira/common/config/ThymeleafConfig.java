package com.javarush.jira.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.util.Locale;
import java.util.Set;

@Configuration
//http://www.thymeleaf.org/doc/articles/thymeleaf3migration.html
@RequiredArgsConstructor
public class ThymeleafConfig {
    private final AppProperties appProperties;

    @Bean
    // Attention: with TemplateEngine clear cache doesn't work
    public SpringTemplateEngine thymeleafTemplateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        FileTemplateResolver viewResolver = createTemplateResolver("./resources/view/");
        viewResolver.setCheckExistence(true);
        viewResolver.setOrder(1);
        FileTemplateResolver mailResolver = createTemplateResolver("./resources/mails/");
        mailResolver.setOrder(2);
        engine.setTemplateResolvers(Set.of(viewResolver, mailResolver));
        return engine;
    }

    private FileTemplateResolver createTemplateResolver(String pfx) {
        return new FileTemplateResolver() {{
            setPrefix(pfx);
            setSuffix(".html");
            setTemplateMode(TemplateMode.HTML);
            setCacheable(true);
            setCacheTTLMs(appProperties.getTemplatesUpdateCache().toMillis());
            setCharacterEncoding("UTF-8");
        }};
    }

    @Bean
    public LocaleResolver localeResolver() { //TODO: 11.add localization
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.US);
        return slr;
    }
}
