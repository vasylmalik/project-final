package com.javarush.jira;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Value;

@SpringBootTest
public class LoadEnvTest {

    @Value("${DB_URL:NOT_SET}")
    private String dbUrl;

    @Value("${DB_USER_NAME:NOT_SET}")
    private String dbUserName;

    @Value("${DB_USER_PWD:NOT_SET}")
    private String dbUserPwd;

    @Value("${GITHUB_CLIENT_ID:NOT_SET}")
    private String githubClientId;

    @Value("${GITHUB_CLIENT_SECRET:NOT_SET}")
    private String githubClientSecret;

    @Value("${GOOGLE_CLIENT_ID:NOT_SET}")
    private String googleClientId;

    @Value("${GOOGLE_CLIENT_SECRET:NOT_SET}")
    private String googleClientSecret;

    @Value("${GITLAB_CLIENT_ID:NOT_SET}")
    private String gitlabClientId;

    @Value("${GITLAB_CLIENT_SECRET:NOT_SET}")
    private String gitlabClientSecret;

    @Value("${MAIL_USER_NAME:NOT_SET}")
    private String mailUserName;

    @Value("${MAIL_PASSWORD:NOT_SET}")
    private String mailPassword;

    @Test
    void printAllEnvVariables() {
        System.out.println("\n=== ENV VARIABLES CHECK ===");
        System.out.println("DB_URL: " + dbUrl);
        System.out.println("DB_USER_NAME: " + dbUserName);
        System.out.println("DB_USER_PWD: " + dbUserPwd);
        System.out.println("GITHUB_CLIENT_ID: " + githubClientId);
        System.out.println("GITHUB_CLIENT_SECRET: " + githubClientSecret);
        System.out.println("GOOGLE_CLIENT_ID: " + googleClientId);
        System.out.println("GOOGLE_CLIENT_SECRET: " + googleClientSecret);
        System.out.println("GITLAB_CLIENT_ID: " + gitlabClientId);
        System.out.println("GITLAB_CLIENT_SECRET: " + gitlabClientSecret);
        System.out.println("MAIL_USER_NAME: " + mailUserName);
        System.out.println("MAIL_PASSWORD: " + mailPassword);
        System.out.println("============================\n");
    }
}
