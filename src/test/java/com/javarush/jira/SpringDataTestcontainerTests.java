package com.javarush.jira;

import jakarta.validation.constraints.NotNull;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest
@ContextConfiguration(initializers =
        SpringDataTestcontainerTests
                .DockerPostgresDataInitializer.class)
public class SpringDataTestcontainerTests {
    static PostgreSQLContainer<?> postgreSQLContainer;

    static {

        postgreSQLContainer = new PostgreSQLContainer<>("postgres:16")
                .withUsername("jira")
                .withPassword("JiraRush")
                .withDatabaseName("jira-test")
                .withReuse(true);


        postgreSQLContainer.start();
    }
    public static class DockerPostgresDataInitializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        String jdbcUrl = "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl();
        String username = "spring.datasource.username=" + postgreSQLContainer.getUsername();
        String password = "spring.datasource.password=" + postgreSQLContainer.getPassword();

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {

            TestPropertySourceUtils
                    .addInlinedPropertiesToEnvironment(applicationContext, jdbcUrl, username, password);
        }
    }

    @Test
    void contextLoads() throws SQLException {

        ResultSet resultSet = performQuery(postgreSQLContainer);
        resultSet.next();
        int result = resultSet.getInt(1);
        assertEquals(1, result);

        Assertions.assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    private ResultSet performQuery(@SuppressWarnings("rawtypes") PostgreSQLContainer postgreSQLContainer)
            throws SQLException {

        String query = "SELECT 1";

        String jdbcUrl = postgreSQLContainer.getJdbcUrl();
        String username = postgreSQLContainer.getUsername();
        String password = postgreSQLContainer.getPassword();

        Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
        return conn.createStatement().executeQuery(query);
    }
}
