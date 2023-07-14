package com.javarush.jira.bugtracking.task;

import com.javarush.jira.MatcherFactory;
import com.javarush.jira.bugtracking.UserBelong;
import com.javarush.jira.bugtracking.task.to.ActivityTo;
import com.javarush.jira.bugtracking.task.to.TaskTo;
import com.javarush.jira.bugtracking.task.to.TaskToExt;
import com.javarush.jira.bugtracking.task.to.TaskToFull;
import com.javarush.jira.common.to.CodeTo;

import java.util.List;

import static com.javarush.jira.bugtracking.ObjectType.TASK;
import static com.javarush.jira.login.internal.web.UserTestData.ADMIN_ID;
import static com.javarush.jira.login.internal.web.UserTestData.USER_ID;

public class TaskTestData {
    public static final MatcherFactory.Matcher<Task> TASK_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Task.class, "id", "startpoint", "endpoint", "activities", "project", "sprint", "parent", "tags");
    public static final MatcherFactory.Matcher<TaskTo> TASK_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(TaskTo.class, "id", "startpoint", "endpoint");
    public static final MatcherFactory.Matcher<TaskToFull> TASK_TO_FULL_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(TaskToFull.class, "id", "updated", "activityTos.id");
    public static final MatcherFactory.Matcher<Activity> ACTIVITY_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Activity.class, "title", "updated", "author");
    public static final MatcherFactory.Matcher<UserBelong> USER_BELONG_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(UserBelong.class, "id", "startpoint", "endpoint");

    public static final long TASK1_ID = 1;
    public static final long TASK2_ID = 2;
    public static final long READY_FOR_TEST_TASK_ID = 3;
    public static final long READY_FOR_REVIEW_TASK_ID = 4;
    public static final long TODO_TASK_ID = 5;
    public static final long DONE_TASK_ID = 6;
    public static final long CANCELED_TASK_ID = 7;
    public static final long SPRINT1_ID = 1;
    public static final long PROJECT1_ID = 1;
    public static final long ACTIVITY1_ID = 1;
    public static final long NOT_FOUND = 100;
    public static final String TODO = "todo";
    public static final String IN_PROGRESS = "in_progress";
    public static final String READY_FOR_REVIEW = "ready_for_review";
    public static final String READY_FOR_TEST = "ready_for_test";
    public static final String TEST = "test";
    public static final String DONE = "done";
    public static final String CANCELED = "canceled";
    public static final String TASK_DEVELOPER = "task_developer";
    public static final String TASK_REVIEWER = "task_reviewer";

    public static final TaskTo taskTo1 = new TaskTo(TASK1_ID, "epic-" + TASK1_ID, "Data", "epic", "in_progress", null, PROJECT1_ID, SPRINT1_ID);
    public static final TaskTo taskTo2 = new TaskTo(TASK2_ID, "epic-" + TASK2_ID, "Trees", "epic", "in_progress", null, PROJECT1_ID, SPRINT1_ID);
    public static final TaskToFull taskToFull1 = new TaskToFull(TASK1_ID, "epic-1", "Data", null, "epic", "in_progress", "normal", null, 4, null, new CodeTo(PROJECT1_ID, "PR1"), new CodeTo(SPRINT1_ID, "SP-1.001"), null);
    public static final TaskToFull taskToFull2 = new TaskToFull(TASK2_ID, "epic-2", "Trees UPD", "task UPD", "epic", "ready_for_review", "high", null, 4, null, new CodeTo(PROJECT1_ID, "PR1"), new CodeTo(SPRINT1_ID, "SP-1.001"), null);
    public static final ActivityTo activityTo1ForTask1 = new ActivityTo(ACTIVITY1_ID, TASK1_ID, USER_ID, null, null, "in_progress", "low", "epic", "Data", null, 3, null);
    public static final ActivityTo activityTo2ForTask1 = new ActivityTo(ACTIVITY1_ID + 1, TASK1_ID, ADMIN_ID, null, null, null, "normal", null, "Data", null, null, null);
    public static final ActivityTo activityTo3ForTask1 = new ActivityTo(ACTIVITY1_ID + 2, TASK1_ID, USER_ID, null, null, null, null, null, "Data", null, 4, null);
    public static final List<ActivityTo> activityTosForTask1 = List.of(activityTo3ForTask1, activityTo2ForTask1, activityTo1ForTask1);
    public static final ActivityTo activityTo1ForTask2 = new ActivityTo(ACTIVITY1_ID + 3, TASK2_ID, USER_ID, null, null, "in_progress", "normal", "epic", "Trees", "Trees desc", 4, null);
    public static final ActivityTo updatePriorityCode = new ActivityTo(ACTIVITY1_ID + 4, TASK2_ID, USER_ID, null, null, "ready_for_review", "high", "epic", "Trees UPD", "task UPD", 4, null);
    public static final List<ActivityTo> activityTosForTask2 = List.of(updatePriorityCode, activityTo1ForTask2);

    public static final UserBelong userTask1Assignment1 = new UserBelong(1L, TASK, USER_ID, "task_developer");
    public static final UserBelong userTask1Assignment2 = new UserBelong(1L, TASK, USER_ID, "task_tester");
    public static final UserBelong userTask2Assignment1 = new UserBelong(2L, TASK, USER_ID, "task_developer");
    public static final UserBelong userTask2Assignment2 = new UserBelong(2L, TASK, USER_ID, "task_tester");

    static {
        taskToFull1.setActivityTos(activityTosForTask1);
        taskToFull2.setActivityTos(activityTosForTask2);
    }

    public static TaskToExt getNewTaskTo() {
        return new TaskToExt(null, "epic-1", "Data New", "task NEW", "epic", "in_progress", "low", null, 3, null, PROJECT1_ID, SPRINT1_ID);
    }

    public static ActivityTo getNewActivityTo() {
        return new ActivityTo(null, TASK1_ID, USER_ID, null, null, "ready_for_review", null, "epic", null, null, 4, null);
    }

    public static TaskToExt getUpdatedTaskTo() {
        return new TaskToExt(TASK2_ID, "epic-2", "Trees UPD", "task UPD", "epic", "ready_for_review", "high", null, 4, null, PROJECT1_ID, SPRINT1_ID);
    }

    public static ActivityTo getUpdatedActivityTo() {
        return new ActivityTo(ACTIVITY1_ID, TASK1_ID, USER_ID, null, null, "in_progress", "low", "epic", null, null, 3, null);
    }
}
