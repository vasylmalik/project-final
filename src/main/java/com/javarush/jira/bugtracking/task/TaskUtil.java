package com.javarush.jira.bugtracking.task;

import com.javarush.jira.bugtracking.task.to.TaskDTOExt;
import com.javarush.jira.bugtracking.task.to.TaskDTOFull;
import com.javarush.jira.common.error.DataConflictException;
import com.javarush.jira.login.AuthUser;
import com.javarush.jira.ref.RefDTO;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.javarush.jira.ref.RefType.TASK_STATUS;
import static com.javarush.jira.ref.ReferenceService.getRefs;

public class TaskUtil {

    static Map<String, RefDTO> getPossibleStatusRefs(String currentStatus) {
        Set<String> possibleStatuses = getPossibleStatuses(currentStatus);
        return getRefs(TASK_STATUS).entrySet().stream()
                .filter(ref -> possibleStatuses.contains(ref.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (ref1, ref2) -> ref1, LinkedHashMap::new));
    }

    static void checkStatusChangePossible(String currentStatus, String newStatus) {
        if (!getPossibleStatuses(currentStatus).contains(newStatus)) {
            throw new DataConflictException("Cannot change task status from " + currentStatus + " to " + newStatus);
        }
    }

    private static Set<String> getPossibleStatuses(String currentStatus) {
        Set<String> possibleStatuses = new HashSet<>();
        possibleStatuses.add(currentStatus);
        Map<String, RefDTO> taskStatusRefs = getRefs(TASK_STATUS);
        String aux = taskStatusRefs.get(currentStatus).getAux(0);
        possibleStatuses.addAll(aux == null ? Set.of() : Set.of(aux.split(",")));
        return possibleStatuses;
    }

    static void fillExtraFields(TaskDTOFull taskDTOFull, List<Activity> activities) {
        if (!activities.isEmpty()) {
            taskDTOFull.setUpdated(activities.get(0).getUpdated());
            for (Activity latest : activities) {
                if (taskDTOFull.getDescription() == null && latest.getDescription() != null) {
                    taskDTOFull.setDescription(latest.getDescription());
                }
                if (taskDTOFull.getPriorityCode() == null && latest.getPriorityCode() != null) {
                    taskDTOFull.setPriorityCode(latest.getPriorityCode());
                }
                if (taskDTOFull.getEstimate() == null && latest.getEstimate() != null) {
                    taskDTOFull.setEstimate(latest.getEstimate());
                }
                if (taskDTOFull.getDescription() != null && taskDTOFull.getPriorityCode() != null && taskDTOFull.getEstimate() != null)
                    break;
            }
        }
    }

    static String getLatestValue(List<Activity> activities, Function<Activity, String> valueExtractFunction) {
        for (Activity activity : activities) {
            String value = valueExtractFunction.apply(activity);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    static Activity makeActivity(long taskId, TaskDTOExt taskDTOExt) {
        return new Activity(null, taskId, AuthUser.authId(), null, null, taskDTOExt.getStatusCode(), taskDTOExt.getPriorityCode(),
                taskDTOExt.getTypeCode(), taskDTOExt.getTitle(), taskDTOExt.getDescription(), taskDTOExt.getEstimate());
    }
}
