//package com.javarush.jira.common.internal.config;
//
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class DataSourceConfig {
//
//    // Цей бін створиться, лише якщо активний профіль "test"
//    @Bean
//    @Profile("test")
//    public DataSource testDataSource() {
//        return DataSourceBuilder.create()
//                .driverClassName("org.h2.Driver")
//                .url("jdbc:h2:mem:jira_test;DB_CLOSE_DELAY=-1;MODE=PostgreSQL;NON_KEYWORDS=USER")
//                .username("sa")
//                .password("")
//                .build();
//    }
//
//    // Цей бін створиться, якщо профіль "test" НЕ активний (наприклад, dev, prod або default)
//    @Bean
//    @Profile("!test")
//    public DataSource primaryDataSource() {
//        // Зчитуємо системні змінні оточення для PostgreSQL
//        String url = System.getProperty("spring.datasource.url", "jdbc:postgresql://postgres-db:5432/jira");
//        String username = System.getenv("DB_USERNAME");
//        String password = System.getenv("DB_PASSWORD");
//
//        return DataSourceBuilder.create()
//                .driverClassName("org.postgresql.Driver")
//                .url(url)
//                .username(username != null ? username : "postgres")
//                .password(password != null ? password : "password")
//                .build();
//    }
//}

package com.javarush.jira.common.internal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

// Зчитуємо змінні з application-test.yaml
@Value("${spring.datasource.url}")
private String testUrl;

    @Value("${spring.datasource.username}")
    private String testUsername;

    @Value("${spring.datasource.password}")
    private String testPassword;

    @Value("${spring.datasource.driver-class-name}")
    private String testDriver;

    @Bean
    @Profile("test")
    public DataSource testDataSource() {
        return DataSourceBuilder.create()
                .driverClassName(testDriver)
                .url(testUrl)
                .username(testUsername)
                .password(testPassword)
                .build();
    }

    // ДЛЯ ПРОДАКШЕНУ/DEV (залишаємо як було)
    @Bean
    @Profile("!test")
    public DataSource primaryDataSource() {
        String url = System.getProperty("spring.datasource.url", "jdbc:postgresql://postgres-db:5432/jira");
        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");

        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url(url)
                .username(username != null ? username : "postgres")
                .password(password != null ? password : "password")
                .build();
    }
}