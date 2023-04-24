package com.javarush.jira;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

@SpringBootTest
@Sql(scripts = {
		"classpath:db/changelog/001_init_schema.sql",
		"classpath:db/002_late_data.sql",
		"classpath:db/003_add_dashboard.sql"},
		config = @SqlConfig(encoding = "UTF-8"))
@AutoConfigureMockMvc
@ActiveProfiles("test")
class JiraRushApplicationTests {
	@Test
	void contextLoads() {
	}
}
