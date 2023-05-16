package com.javarush.jira.bugtracking;

import com.javarush.jira.bugtracking.internal.model.Activity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

public class TaskUtilTest {

    @Test
    public void checkTakeMsBetweenTwoActivitiesWithEmptyActivities() {
        // given
        // when
        long actualMsDiff = TaskUtil.takeMsBetweenTwoActivitiesByStatusCode(Collections.emptyList(), "", "");
        // then
        Assertions.assertEquals(-1, actualMsDiff);
    }

    @Test
    public void checkTakeMsInProgress() {
        // given
        long expectedMsDiff = 3 * 24 * 60 * 60 * 1000;
        LocalDateTime inProgressDate = LocalDateTime.of(2023, Month.MAY, 1, 14, 30);
        LocalDateTime readyDate = inProgressDate.plus(expectedMsDiff, ChronoUnit.MILLIS);

        List<Activity> activityList = List.of(
                prepareActivity("in progress", inProgressDate),
                prepareActivity("ready", readyDate)
        );
        // when
        long actualMsDiff = TaskUtil.takeMsInProgress(activityList);
        // then
        Assertions.assertEquals(expectedMsDiff, actualMsDiff);
    }

    @Test
    public void checkTakeMsInTesting() {
        // given
        long expectedMsDiff = 3 * 24 * 60 * 60 * 1000;
        LocalDateTime readyDate = LocalDateTime.of(2023, Month.MAY, 1, 14, 30);
        LocalDateTime doneDate = readyDate.plus(expectedMsDiff, ChronoUnit.MILLIS);

        List<Activity> activityList = List.of(
                prepareActivity("ready", readyDate),
                prepareActivity("done", doneDate)
        );
        // when
        long actualMsDiff = TaskUtil.takeMsInTesting(activityList);
        // then
        Assertions.assertEquals(expectedMsDiff, actualMsDiff);
    }

    private Activity prepareActivity(String statusCode, LocalDateTime updated) {
        Activity activity = new Activity();
        activity.setStatusCode(statusCode);
        activity.setUpdated(updated);
        return activity;
    }

}
