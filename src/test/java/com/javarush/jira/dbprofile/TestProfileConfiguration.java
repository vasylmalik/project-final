package com.javarush.jira.dbprofile;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
//@ComponentScan(basePackages = "com.javarush")
//@RequiredArgsConstructor
////@PropertySource("/application-test.yaml")
//@TestPropertySource("/application-test.yaml")
//@Profile("test")
//@Configuration
public class TestProfileConfiguration {
//    @Autowired
//    private Environment env;
//
//    @Bean
//    public DataSource h2DataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
//        dataSource.setUrl(env.getProperty("spring.datasource.url"));
//        dataSource.setUsername(env.getProperty("spring.datasource.username"));
//        dataSource.setPassword(env.getProperty("spring.datasource.password"));
//        return dataSource;
//    }
}
