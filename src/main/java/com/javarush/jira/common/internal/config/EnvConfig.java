package com.javarush.jira.common.internal.config;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {
    public static void loadEnv() {
        Dotenv dotenv = Dotenv.configure().load();
        System.setProperty("POSTGRES_USER", dotenv.get("POSTGRES_USER"));
        System.setProperty("POSTGRES_PASSWORD", dotenv.get("POSTGRES_PASSWORD"));
        System.setProperty("POSTGRES_PASSWORD", dotenv.get("POSTGRES_PASSWORD"));
        System.setProperty("MAILUSERNAME", dotenv.get("MAILUSERNAME"));
        System.setProperty("MAILPASSWORD", dotenv.get("MAILPASSWORD"));
        System.setProperty("GOOGLE", dotenv.get("GOOGLE"));
        System.setProperty("GITLAB", dotenv.get("GITLAB"));
        System.setProperty("GITHUB", dotenv.get("GITHUB"));

    }
}
