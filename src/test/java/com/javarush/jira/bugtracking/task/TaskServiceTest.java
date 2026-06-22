package com.javarush.jira.bugtracking.task;

import com.javarush.jira.BaseTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest extends BaseTests {

    @Autowired
    private TaskService taskService;

    @Test
    void getTimeInProgress() {
        Duration inProgress = taskService.getTimeInProgress(6L);
        assertNotNull(inProgress);
        assertEquals(781200, inProgress.getSeconds());
    }

    @Test
    void getTimeInTesting() {
        Duration inTesting = taskService.getTimeInTesting(6L);
        assertNotNull(inTesting);
        assertEquals(479100, inTesting.getSeconds());
    }
}