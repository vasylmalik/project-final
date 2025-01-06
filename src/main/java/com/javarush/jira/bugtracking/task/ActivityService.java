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
import java.util.List;

import static com.javarush.jira.bugtracking.task.TaskUtil.getLatestValue;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final TaskRepository taskRepository;

    private final Handlers.ActivityHandler handler;

    public static final String IN_PROGRESS = "in_progress";
    public static final String READY_FOR_REVIEW = "ready_for_review";
    public static final String DONE = "done";

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

    public Duration progressTaskDuration(Task task) {
        LocalDateTime readyForReview = getActivityUpdatedTime(task.id(), READY_FOR_REVIEW);
        LocalDateTime inProgress = getActivityUpdatedTime(task.id(), IN_PROGRESS);

        if (readyForReview == null || inProgress == null) {
            return Duration.ZERO;
        }

        return Duration.between(inProgress, readyForReview);
    }

    public Duration testingTaskDuration(Task task) {
        LocalDateTime done = getActivityUpdatedTime(task.id(), DONE);
        LocalDateTime readyForReview = getActivityUpdatedTime(task.id(), READY_FOR_REVIEW);

        if (readyForReview == null || done == null) {
            return Duration.ZERO;
        }

        return Duration.between(done, readyForReview);
    }

    private LocalDateTime getActivityUpdatedTime(long taskId, String statusCode) {
        return handler.getRepository()
                .findActivityByTaskIdAndStatusCode(taskId, statusCode)
                .map(Activity::getUpdated)
                .orElse(null);
    }
}
