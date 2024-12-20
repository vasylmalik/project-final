package com.javarush.jira.bugtracking.task;

import com.javarush.jira.MatcherFactory;
import com.javarush.jira.bugtracking.UserBelong;
import com.javarush.jira.bugtracking.task.to.ActivityDTO;
import com.javarush.jira.bugtracking.task.to.TaskDTO;
import com.javarush.jira.bugtracking.task.to.TaskDTOExt;
import com.javarush.jira.bugtracking.task.to.TaskDTOFull;
import com.javarush.jira.common.to.CodeDTO;

import java.util.List;
import java.util.Set;

import static com.javarush.jira.bugtracking.ObjectType.TASK;
import static com.javarush.jira.login.internal.web.UserTestData.ADMIN_ID;
import static com.javarush.jira.login.internal.web.UserTestData.USER_ID;

public class TaskTestData {
    public static final MatcherFactory.Matcher<Task> TASK_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Task.class, "id", "startpoint", "endpoint", "activities", "project", "sprint", "parent", "tags");
    public static final MatcherFactory.Matcher<TaskDTO> TASK_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(TaskDTO.class, "id", "startpoint", "endpoint");
    public static final MatcherFactory.Matcher<TaskDTOFull> TASK_TO_FULL_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(TaskDTOFull.class, "id", "updated", "activityDTOs.id");
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

    public static final TaskDTO TASK_DTO_1 = new TaskDTO(TASK1_ID, "epic-" + TASK1_ID, "Data", "epic", "in_progress", null, PROJECT1_ID, SPRINT1_ID);
    public static final TaskDTO TASK_DTO_2 = new TaskDTO(TASK2_ID, "epic-" + TASK2_ID, "Trees", "epic", "in_progress", null, PROJECT1_ID, SPRINT1_ID);
    public static final TaskDTOFull taskDTOFull1 = new TaskDTOFull(TASK1_ID, "epic-1", "Data", null, "epic", "in_progress", "normal", null, 4, null, new CodeDTO(PROJECT1_ID, "PR1"), new CodeDTO(SPRINT1_ID, "SP-1.001"), null);
    public static final TaskDTOFull taskDTOFull2 = new TaskDTOFull(TASK2_ID, "epic-2", "Trees UPD", "task UPD", "epic", "ready_for_review", "high", null, 4, null, new CodeDTO(PROJECT1_ID, "PR1"), new CodeDTO(SPRINT1_ID, "SP-1.001"), null);
    public static final ActivityDTO ACTIVITY_DTO_1_FOR_TASK_1 = new ActivityDTO(ACTIVITY1_ID, TASK1_ID, USER_ID, null, null, "in_progress", "low", "epic", "Data", null, 3, null);
    public static final ActivityDTO ACTIVITY_DTO_2_FOR_TASK_1 = new ActivityDTO(ACTIVITY1_ID + 1, TASK1_ID, ADMIN_ID, null, null, null, "normal", null, "Data", null, null, null);
    public static final ActivityDTO ACTIVITY_DTO_3_FOR_TASK_1 = new ActivityDTO(ACTIVITY1_ID + 2, TASK1_ID, USER_ID, null, null, null, null, null, "Data", null, 4, null);
    public static final List<ActivityDTO> activityDTOsForTask1 = List.of(ACTIVITY_DTO_3_FOR_TASK_1, ACTIVITY_DTO_2_FOR_TASK_1, ACTIVITY_DTO_1_FOR_TASK_1);
    public static final ActivityDTO ACTIVITY_DTO_1_FOR_TASK_2 = new ActivityDTO(ACTIVITY1_ID + 3, TASK2_ID, USER_ID, null, null, "in_progress", "normal", "epic", "Trees", "Trees desc", 4, null);
    public static final ActivityDTO updatePriorityCode = new ActivityDTO(ACTIVITY1_ID + 4, TASK2_ID, USER_ID, null, null, "ready_for_review", "high", "epic", "Trees UPD", "task UPD", 4, null);
    public static final List<ActivityDTO> activityDTOsForTask2 = List.of(updatePriorityCode, ACTIVITY_DTO_1_FOR_TASK_2);

    public static final UserBelong userTask1Assignment1 = new UserBelong(1L, TASK, USER_ID, "task_developer");
    public static final UserBelong userTask1Assignment2 = new UserBelong(1L, TASK, USER_ID, "task_tester");
    public static final UserBelong userTask2Assignment1 = new UserBelong(2L, TASK, USER_ID, "task_developer");
    public static final UserBelong userTask2Assignment2 = new UserBelong(2L, TASK, USER_ID, "task_tester");

    // data for test tags
    public static final Set<String> tags_data = Set.of("tag1", "tag2", "tag3");
    public static final Set<String> tags_data_to_add = Set.of("tag4", "tag5");
    public static final Set<String> tags_data_after_add = Set.of("tag1", "tag2", "tag3", "tag4", "tag5");
    public static final Set<String> tags_for_replace = Set.of("tag6", "tag7");
    public static final String tag_for_single_delete = "tag2";
    public static final Set<String> tags_after_single_delete = Set.of("tag1", "tag3");


    static {
        taskDTOFull1.setActivityDTOs(activityDTOsForTask1);
        taskDTOFull2.setActivityDTOs(activityDTOsForTask2);
        taskDTOFull1.setTags(tags_data);
    }

    public static TaskDTOExt getNewTaskDTO() {
        return new TaskDTOExt(null, "epic-1", "Data New", "task NEW", "epic", "in_progress", "low", null, 3, null, PROJECT1_ID, SPRINT1_ID);
    }

    public static ActivityDTO getNewActivityDTO() {
        return new ActivityDTO(null, TASK1_ID, USER_ID, null, null, "ready_for_review", null, "epic", null, null, 4, null);
    }

    public static TaskDTOExt getUpdatedTaskDTO() {
        return new TaskDTOExt(TASK2_ID, "epic-2", "Trees UPD", "task UPD", "epic", "ready_for_review", "high", null, 4, null, PROJECT1_ID, SPRINT1_ID);
    }

    public static ActivityDTO getUpdatedActivityDTO() {
        return new ActivityDTO(ACTIVITY1_ID, TASK1_ID, USER_ID, null, null, "in_progress", "low", "epic", null, null, 3, null);
    }
}
