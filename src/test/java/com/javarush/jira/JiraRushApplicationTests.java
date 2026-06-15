package com.javarush.jira;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.TimeZone;

@SpringBootTest
@ActiveProfiles("test") // Примусово вмикає профіль "test" для цього зрізу тестів
class JiraRushApplicationTests extends BaseTests {
    @Test
    void contextLoads() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Kyiv"));
    }
}
