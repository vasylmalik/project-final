package com.javarush.jira.common.internal.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
public class DbConfig {

    @Bean
    @Profile("prod")
    public DataSource postgresDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5432/jira")
                .username("jira")
                .password("JiraRush")
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    @Bean
    @Profile("test")
    public DataSource h2DataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:h2:mem:jiradest;DB_CLOSE_DELAY=-1;MODE=PostgreSQL;NON_KEYWORDS=VALUE,PASSWORD,EMAIL")
                .username("sa")
                .password("")
                .driverClassName("org.h2.Driver")
                .build();
    }
}
