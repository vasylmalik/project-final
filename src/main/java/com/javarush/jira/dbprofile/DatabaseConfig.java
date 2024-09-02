package com.javarush.jira.dbprofile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
//@ConfigurationProperties("spring.datasource")
public class DatabaseConfig {
    // @Autowired
   // private Environment env;

    //    @Bean
//    public DataSource postgresDataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
//        dataSource.setUrl(env.getProperty("spring.datasource.url"));
//        dataSource.setUsername(env.getProperty("spring.datasource.username"));
//        dataSource.setPassword(env.getProperty("spring.datasource.password"));
//        return dataSource;
//    }
    @Profile("test")
    @Bean
    public String developmentConnectionStrings() {
        System.out.println("DB connection for 'test'");

        return "test Enviroment Connected!";
    }

    @Profile("prod")
    @Bean
    public String productionConnectionStrings() {
        System.out.println("DB connection for 'prod'");

        return "prod Enviroment Connected!";
    }
}
