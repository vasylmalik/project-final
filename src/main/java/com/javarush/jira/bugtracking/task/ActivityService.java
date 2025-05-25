package com.javarush.jira.bugtracking.task;

import com.javarush.jira.bugtracking.Handlers;
import com.javarush.jira.bugtracking.task.to.ActivityTo;
import com.javarush.jira.common.error.DataConflictException;
import com.javarush.jira.login.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static com.javarush.jira.bugtracking.task.TaskUtil.getLatestValue;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final TaskRepository taskRepository;

    private final Handlers.ActivityHandler handler;

    private static void checkBelong(HasAuthorId activity) {
        if (activity.getAuthorId() != AuthUser.authId()) {
            throw new DataConflictException("Activity " + activity.getId() + " doesn't belong to " + AuthUser.get());
        }
    }

    @Transactional
    public Activity create(ActivityTo activityTo) {
        checkBelong(activityTo);
        Task task = taskRepository.getExisted(activityTo.getTaskId());
        if (activityTo.getStatusCode() != null) {
            task.checkAndSetStatusCode(activityTo.getStatusCode());
        }
        if (activityTo.getTypeCode() != null) {
            task.setTypeCode(activityTo.getTypeCode());
        }
        return handler.createFromTo(activityTo);
    }

    @Transactional
    public void update(ActivityTo activityTo, long id) {
        checkBelong(handler.getRepository().getExisted(activityTo.getId()));
        handler.updateFromTo(activityTo, id);
        updateTaskIfRequired(activityTo.getTaskId(), activityTo.getStatusCode(), activityTo.getTypeCode());
    }

    @Transactional
    public void delete(long id) {
        Activity activity = handler.getRepository().getExisted(id);
        checkBelong(activity);
        handler.delete(activity.id());
        updateTaskIfRequired(activity.getTaskId(), activity.getStatusCode(), activity.getTypeCode());
    }

    private void updateTaskIfRequired(long taskId, String activityStatus, String activityType) {
        if (activityStatus != null || activityType != null) {
            Task task = taskRepository.getExisted(taskId);
            List<Activity> activities = handler.getRepository().findAllByTaskIdOrderByUpdatedDesc(task.id());
            if (activityStatus != null) {
                String latestStatus = getLatestValue(activities, Activity::getStatusCode);
                if (latestStatus == null) {
                    throw new DataConflictException("Primary activity cannot be delete or update with null values");
                }
                task.setStatusCode(latestStatus);
            }
            if (activityType != null) {
                String latestType = getLatestValue(activities, Activity::getTypeCode);
                if (latestType == null) {
                    throw new DataConflictException("Primary activity cannot be delete or update with null values");
                }
                task.setTypeCode(latestType);
            }
        }
    }

    public Duration timeSpentOnTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }

        List<Activity> activities = task.getActivities();
        if (activities == null || activities.isEmpty()) {
            return Duration.ZERO;
        }

        List<Activity> sortedActivities = activities.stream()
                .filter(activity -> activity.getStatusCode() != null && activity.getUpdated() != null)
                .sorted(Comparator.comparing(Activity::getUpdated))
                .toList();

        Duration totalDuration = Duration.ZERO;
        LocalDateTime inProgressTime = null;

        for (Activity activity : sortedActivities) {
            String statusCode = activity.getStatusCode();
            LocalDateTime updated = activity.getUpdated();

            if ("in_progress".equals(statusCode)) {
                inProgressTime = updated;
            } else if ("ready_for_review".equals(statusCode) && inProgressTime != null) {
                totalDuration = totalDuration.plus(Duration.between(inProgressTime, updated));
                inProgressTime = null;
            }
        }

        return totalDuration;
    }

    public Duration timeSpentOnTest(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }

        List<Activity> activities = task.getActivities();
        if (activities == null || activities.isEmpty()) {
            return Duration.ZERO;
        }

        List<Activity> sortedActivities = activities.stream()
                .filter(activity -> activity.getStatusCode() != null && activity.getUpdated() != null)
                .sorted(Comparator.comparing(Activity::getUpdated))
                .toList();

        Duration totalDuration = Duration.ZERO;
        LocalDateTime readyForReviewTime = null;

        for (Activity activity : sortedActivities) {
            String statusCode = activity.getStatusCode();
            LocalDateTime updated = activity.getUpdated();

            if ("ready_for_review".equals(statusCode)) {
                readyForReviewTime = updated;
            } else if ("done".equals(statusCode) && readyForReviewTime != null) {
                totalDuration = totalDuration.plus(Duration.between(readyForReviewTime, updated));
                readyForReviewTime = null;
            }
        }

        return totalDuration;
    }
}
