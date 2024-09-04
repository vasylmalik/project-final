package com.javarush.jira.common.internal.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@PropertySource(value = "classpath:application.yaml", factory = EnvPropertySourceLoader.class)
public class EnvConfig {
    @Bean
    public Dotenv dotenv() {
        Dotenv dotenv = Dotenv.configure().load();
        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );
        return dotenv;
    }

}
