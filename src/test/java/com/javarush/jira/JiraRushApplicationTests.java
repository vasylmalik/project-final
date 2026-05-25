package com.javarush.jira;

import org.junit.jupiter.api.Test;

import java.util.TimeZone;

class JiraRushApplicationTests extends BaseTests {
    @Test
    void contextLoads() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Kyiv"));
    }
}
