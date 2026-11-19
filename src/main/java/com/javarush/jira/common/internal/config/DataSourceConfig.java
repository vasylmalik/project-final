package com.javarush.jira.common.internal.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    @Profile("!test")
    public DataSource postgresDataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean
    @Profile("test")
    public DataSource h2DataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }
}
