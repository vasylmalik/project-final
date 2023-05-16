package com.javarush.jira.bugtracking;

import com.javarush.jira.bugtracking.internal.model.Activity;
import lombok.experimental.UtilityClass;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class TaskUtil {

    public static long takeMsInProgress(List<Activity> activityList) {
        return takeMsBetweenTwoActivitiesByStatusCode(activityList, "in progress", "ready");
    }

    public static long takeMsInTesting(List<Activity> activityList) {
        return takeMsBetweenTwoActivitiesByStatusCode(activityList, "ready", "done");
    }

    public static long takeMsBetweenTwoActivitiesByStatusCode(List<Activity> activityList, String beginStatusCode, String endStatusCode) {
        Optional<Activity> readyActivityOptional = takeFistActivityByStatusCode(activityList, beginStatusCode);
        if (readyActivityOptional.isEmpty()) {
            return -1;
        }
        Optional<Activity> inProgressActivityOptional = takeFistActivityByStatusCode(activityList, endStatusCode);
        if (inProgressActivityOptional.isEmpty()) {
            return -1;
        }
        Duration between = Duration.between(readyActivityOptional.get().getUpdated(), inProgressActivityOptional.get().getUpdated());
        return between.toMillis();
    }

    private static Optional<Activity> takeFistActivityByStatusCode(List<Activity> activityList, String statusCode) {
        return activityList.stream()
                .filter(activity -> activity.getUpdated() != null)
                .filter(activity -> statusCode.equals(activity.getStatusCode()))
                .findFirst();
    }

}
