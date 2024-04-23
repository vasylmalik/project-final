package com.javarush.jira.bugtracking.task;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.bugtracking.Handlers;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ActivityServiceTest extends AbstractControllerTest{
    @Test
    public void testCalculateTimeInProgress() {
        Task task = mock(Task.class);
        when(task.getId()).thenReturn(1L);

        List<Activity> activities = new ArrayList<>();
        activities.add(new Activity(1L, 1L, null, LocalDateTime.parse("2023-05-15T12:25:10"), null, "in_progress", null, null, null, null, null));
        activities.add(new Activity(2L, 1L, null, LocalDateTime.parse("2023-05-15T14:05:10"), null, "ready_for_review", null, null, null, null, null));

        Handlers.ActivityHandler handler = mock(Handlers.ActivityHandler.class);
        ActivityRepository activityRepository = mock(ActivityRepository.class);
        when(handler.getRepository()).thenReturn(activityRepository);
        when(activityRepository.findAllByTaskIdOrderByUpdatedDesc(1L)).thenReturn(activities);

        ActivityService activityService = new ActivityService(null, handler);

        Duration timeInTesting = activityService.calculateTimeInProgress(task);
        assertEquals(Duration.ofHours(1).plusMinutes(40), timeInTesting);
    }

    @Test
    public void testCalculateTimeInTesting() {
        Task task = mock(Task.class);
        when(task.getId()).thenReturn(1L);

        List<Activity> activities = new ArrayList<>();
        activities.add(new Activity(1L, 1L, null, LocalDateTime.parse("2023-05-15T12:25:10"), null, "ready_for_review", null, null, null, null, null));
        activities.add(new Activity(2L, 1L, null, LocalDateTime.parse("2023-05-15T14:05:10"), null, "done", null, null, null, null, null));

        Handlers.ActivityHandler handler = mock(Handlers.ActivityHandler.class);
        ActivityRepository activityRepository = mock(ActivityRepository.class);
        when(handler.getRepository()).thenReturn(activityRepository);
        when(activityRepository.findAllByTaskIdOrderByUpdatedDesc(1L)).thenReturn(activities);

        ActivityService activityService = new ActivityService(null, handler);

        Duration timeInTesting = activityService.calculateTimeInTesting(task);
        assertEquals(Duration.ofHours(1).plusMinutes(40), timeInTesting);
    }
}