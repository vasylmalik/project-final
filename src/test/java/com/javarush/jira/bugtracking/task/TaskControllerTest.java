package com.javarush.jira.bugtracking.task;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.bugtracking.UserBelongRepository;
import com.javarush.jira.bugtracking.task.to.ActivityTo;
import com.javarush.jira.bugtracking.task.to.TaskToExt;
import com.javarush.jira.bugtracking.task.to.TaskToFull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.javarush.jira.bugtracking.ObjectType.TASK;
import static com.javarush.jira.bugtracking.task.TaskController.REST_URL;
import static com.javarush.jira.bugtracking.task.TaskService.CANNOT_ASSIGN;
import static com.javarush.jira.bugtracking.task.TaskService.CANNOT_UN_ASSIGN;
import static com.javarush.jira.bugtracking.task.TaskTestData.NOT_FOUND;
import static com.javarush.jira.bugtracking.task.TaskTestData.*;
import static com.javarush.jira.common.util.JsonUtil.writeValue;
import static com.javarush.jira.login.internal.web.UserTestData.*;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskControllerTest extends AbstractControllerTest {
    private static final String TASKS_REST_URL_SLASH = REST_URL + "/";
    private static final String TASKS_BY_PROJECT_REST_URL = REST_URL + "/by-project";
    private static final String TASKS_BY_SPRINT_REST_URL = REST_URL + "/by-sprint";
    private static final String ACTIVITIES_REST_URL = REST_URL + "/activities";
    private static final String ACTIVITIES_REST_URL_SLASH = REST_URL + "/activities/";
    private static final String CHANGE_STATUS = "/change-status";

    private static final String PROJECT_ID = "projectId";
    private static final String SPRINT_ID = "sprintId";
    private static final String STATUS_CODE = "statusCode";
    private static final String USER_TYPE = "userType";
    private static final String ENABLED = "enabled";

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private UserBelongRepository userBelongRepository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        get(TASK1_ID, taskToFull1);
    }

    private void get(long taskId, TaskToFull taskToFull) throws Exception {
        perform(MockMvcRequestBuilders.get(TASKS_REST_URL_SLASH + taskId))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TASK_TO_FULL_MATCHER.contentJson(taskToFull));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(TASKS_REST_URL_SLASH + TASK1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(TASKS_REST_URL_SLASH + NOT_FOUND))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAllBySprint() throws Exception {
        perform(MockMvcRequestBuilders.get(TASKS_BY_SPRINT_REST_URL)
                .param(SPRINT_ID, String.valueOf(SPRINT1_ID)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TASK_TO_MATCHER.contentJson(taskTo2, taskTo1));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAllByProject() throws Exception {
        perform(MockMvcRequestBuilders.get(TASKS_BY_PROJECT_REST_URL)
                .param(PROJECT_ID, String.valueOf(TaskTestData.PROJECT1_ID)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TASK_TO_MATCHER.contentJson(taskTo2, taskTo1));
    }

    @Test
    void getAllByProjectUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(TASKS_BY_PROJECT_REST_URL)
                .param(PROJECT_ID, String.valueOf(TaskTestData.PROJECT1_ID)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateTask() throws Exception {
        TaskToExt updatedTo = TaskTestData.getUpdatedTaskTo();
        perform(MockMvcRequestBuilders.put(TASKS_REST_URL_SLASH + TASK2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        Task updated = new Task(updatedTo.getId(), updatedTo.getTitle(), updatedTo.getTypeCode(), updatedTo.getStatusCode(), updatedTo.getParentId(), updatedTo.getProjectId(), updatedTo.getSprintId());
        TASK_MATCHER.assertMatch(taskRepository.getExisted(TASK2_ID), updated);
        get(TASK2_ID, taskToFull2);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateTaskWhenStateNotChanged() throws Exception {
        int activitiesCount = activityRepository.findAllByTaskIdOrderByUpdatedDesc(TASK2_ID).size();
        TaskToExt sameStateTo = new TaskToExt(TASK2_ID, taskTo2.getCode(), taskTo2.getTitle(), "Trees desc", taskTo2.getTypeCode(),
                taskTo2.getStatusCode(), "normal", null, 4, taskTo2.getParentId(), taskTo2.getProjectId(), taskTo2.getSprintId());
        perform(MockMvcRequestBuilders.put(TASKS_REST_URL_SLASH + TASK2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(sameStateTo)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertEquals(activitiesCount, activityRepository.findAllByTaskIdOrderByUpdatedDesc(TASK2_ID).size());
    }

    @Test
    void updateTaskUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.put(TASKS_REST_URL_SLASH + TASK2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(getUpdatedTaskTo())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateTaskWhenProjectNotExists() throws Exception {
        TaskToExt notExistsProjectTo = new TaskToExt(TASK2_ID, "epic-2", "Trees UPD", "task UPD", "epic", "in_progress", "high", null, 4, null, NOT_FOUND, SPRINT1_ID);
        perform(MockMvcRequestBuilders.put(TASKS_REST_URL_SLASH + TASK2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(notExistsProjectTo)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateTaskIdNotConsistent() throws Exception {
        perform(MockMvcRequestBuilders.put(TASKS_REST_URL_SLASH + (TASK2_ID + 1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(getUpdatedTaskTo())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateTaskWhenChangeProject() throws Exception {
        TaskToExt changedProjectTo = new TaskToExt(TASK2_ID, "epic-2", "Trees UPD", "task UPD", "epic", "in_progress", "high", null, 4, null, PROJECT1_ID + 1, SPRINT1_ID);
        perform(MockMvcRequestBuilders.put(TASKS_REST_URL_SLASH + TASK2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(changedProjectTo)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateSprintIdWhenDev() throws Exception {
        TaskToExt changedSprintTo = new TaskToExt(TASK2_ID, "epic-2", "Trees UPD", "task UPD", "epic", "in_progress", "high", null, 4, null, PROJECT1_ID, SPRINT1_ID + 1);
        perform(MockMvcRequestBuilders.put(TASKS_REST_URL_SLASH + TASK2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(changedSprintTo)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateSprintIdWhenAdmin() throws Exception {
        TaskToExt changedSprintTo = new TaskToExt(TASK2_ID, "epic-2", "Trees UPD", "task UPD", "epic", "in_progress", "high", null, 4, null, PROJECT1_ID, SPRINT1_ID + 1);
        perform(MockMvcRequestBuilders.put(TASKS_REST_URL_SLASH + TASK2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(changedSprintTo)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertEquals(SPRINT1_ID + 1, taskRepository.getExisted(TASK2_ID).getSprintId());
    }

    @Test
    @WithUserDetails(value = MANAGER_MAIL)
    void updateSprintIdWhenManager() throws Exception {
        TaskToExt changedSprintTo = new TaskToExt(TASK2_ID, "epic-2", "Trees UPD", "task UPD", "epic", "in_progress", "high", null, 4, null, PROJECT1_ID, SPRINT1_ID + 1);
        perform(MockMvcRequestBuilders.put(TASKS_REST_URL_SLASH + TASK2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(changedSprintTo)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertEquals(SPRINT1_ID + 1, taskRepository.getExisted(TASK2_ID).getSprintId());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateActivity() throws Exception {
        ActivityTo updatedTo = getUpdatedActivityTo();
        perform(MockMvcRequestBuilders.put(ACTIVITIES_REST_URL_SLASH + ACTIVITY1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        Activity updated = new Activity(updatedTo.getId(), updatedTo.getTaskId(), updatedTo.getAuthorId(), updatedTo.getUpdated(),
                updatedTo.getComment(), updatedTo.getStatusCode(), updatedTo.getPriorityCode(), updatedTo.getTypeCode(), updatedTo.getTitle(),
                updatedTo.getDescription(), updatedTo.getEstimate());
        ACTIVITY_MATCHER.assertMatch(activityRepository.getExisted(ACTIVITY1_ID), updated);
        updateTaskIfRequired(updated.getTaskId(), updated.getStatusCode(), updated.getTypeCode());
    }

    private void updateTaskIfRequired(long taskId, String activityStatus, String activityType) {
        if (activityStatus != null || activityType != null) {
            Task task = taskRepository.getExisted(taskId);
            if (activityStatus != null) assertEquals(task.getStatusCode(), activityStatus);
            if (activityType != null) assertEquals(task.getTypeCode(), activityType);
        }
    }

    @Test
    void updateActivityUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.put(ACTIVITIES_REST_URL_SLASH + ACTIVITY1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(getUpdatedTaskTo())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateWhenTaskNotExists() throws Exception {
        ActivityTo notExistsActivityTo = new ActivityTo(ACTIVITY1_ID, NOT_FOUND, USER_ID, null, null,
                "in_progress", "low", "epic", null, null, 3, null);
        perform(MockMvcRequestBuilders.put(ACTIVITIES_REST_URL_SLASH + ACTIVITY1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(notExistsActivityTo)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateActivityIdNotConsistent() throws Exception {
        perform(MockMvcRequestBuilders.put(ACTIVITIES_REST_URL_SLASH + (ACTIVITY1_ID + 1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(getUpdatedActivityTo())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateActivityWhenChangeTask() throws Exception {
        ActivityTo changedTaskTo = new ActivityTo(ACTIVITY1_ID, TASK1_ID + 1, USER_ID, null, null,
                "in_progress", "low", "epic", null, null, 3, null);
        perform(MockMvcRequestBuilders.put(ACTIVITIES_REST_URL_SLASH + ACTIVITY1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(changedTaskTo)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void enable() throws Exception {
        assertTrue(enable(TASK1_ID, true));
    }

    @Test
    void enableUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.patch(TASKS_REST_URL_SLASH + TASK1_ID)
                .param(ENABLED, "true"))
                .andExpect(status().isUnauthorized());
    }

    private boolean enable(long id, boolean enabled) throws Exception {
        perform(MockMvcRequestBuilders.patch(TASKS_REST_URL_SLASH + id)
                .param(ENABLED, String.valueOf(enabled)))
                .andDo(print())
                .andExpect(status().isNoContent());
        return taskRepository.getExisted(id).isEnabled();
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void disable() throws Exception {
        assertFalse(enable(taskTo2.getId(), false));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void changeTaskStatus() throws Exception {
        perform(MockMvcRequestBuilders.patch(TASKS_REST_URL_SLASH + TASK1_ID + CHANGE_STATUS)
                .param(STATUS_CODE, READY_FOR_REVIEW))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertEquals(READY_FOR_REVIEW, taskRepository.getExisted(TASK1_ID).getStatusCode());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void changeTaskStatusWhenStatusNotChanged() throws Exception {
        int activitiesCount = activityRepository.findAllByTaskIdOrderByUpdatedDesc(TASK1_ID).size();
        perform(MockMvcRequestBuilders.patch(TASKS_REST_URL_SLASH + TASK1_ID + CHANGE_STATUS)
                .param(STATUS_CODE, "in_progress"))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertEquals("in_progress", taskRepository.getExisted(TASK1_ID).getStatusCode());
        assertEquals(activitiesCount, activityRepository.findAllByTaskIdOrderByUpdatedDesc(TASK1_ID).size());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void changeTaskStatusNotFound() throws Exception {
        perform(MockMvcRequestBuilders.patch(TASKS_REST_URL_SLASH + NOT_FOUND + CHANGE_STATUS)
                .param(STATUS_CODE, READY_FOR_REVIEW))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void cannotChangeTaskStatus() throws Exception {
        perform(MockMvcRequestBuilders.patch(TASKS_REST_URL_SLASH + TASK1_ID + CHANGE_STATUS)
                .param(STATUS_CODE, TEST))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void changeTaskStatusUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.patch(TASKS_REST_URL_SLASH + TASK1_ID + CHANGE_STATUS)
                .param(STATUS_CODE, READY_FOR_REVIEW))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createTaskWithLocation() throws Exception {
        TaskToExt newTo = getNewTaskTo();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newTo)))
                .andExpect(status().isCreated());

        Task created = TASK_MATCHER.readFromJson(action);
        long newId = created.id();
        Task newTask = new Task(newId, newTo.getTitle(), newTo.getTypeCode(), newTo.getStatusCode(), newTo.getParentId(), newTo.getProjectId(), newTo.getSprintId());
        TASK_MATCHER.assertMatch(created, newTask);
        TASK_MATCHER.assertMatch(taskRepository.getExisted(newId), newTask);
    }

    @Test
    void createTaskUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(getNewTaskTo())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createTaskInvalid() throws Exception {
        TaskToExt invalidTo = new TaskToExt(null, "", null, null, "epic", null, null, null, 3, null, PROJECT1_ID, SPRINT1_ID);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(invalidTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createTaskWhenProjectNotExists() throws Exception {
        TaskToExt notExistsProjectTo = new TaskToExt(null, "epic-1", "Data New", "task NEW", "epic", "in_progress", "low", null, 3, null, NOT_FOUND, SPRINT1_ID);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(notExistsProjectTo)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void createActivityWithLocation() throws Exception {
        ActivityTo newTo = getNewActivityTo();
        ResultActions action = perform(MockMvcRequestBuilders.post(ACTIVITIES_REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newTo)))
                .andExpect(status().isCreated());

        Activity created = ACTIVITY_MATCHER.readFromJson(action);
        long newId = created.id();
        Activity newActivity = new Activity(newId, newTo.getAuthorId(), newTo.getTaskId(), newTo.getUpdated(), newTo.getComment(),
                newTo.getStatusCode(), newTo.getPriorityCode(), newTo.getTypeCode(), newTo.getTitle(), newTo.getDescription(), newTo.getEstimate());
        ACTIVITY_MATCHER.assertMatch(created, newActivity);
        ACTIVITY_MATCHER.assertMatch(activityRepository.getExisted(newId), newActivity);
        updateTaskIfRequired(created.getTaskId(), created.getStatusCode(), created.getTypeCode());
    }

    @Test
    void createActivityUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.post(ACTIVITIES_REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(getNewTaskTo())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createActivityWhenTaskNotExists() throws Exception {
        ActivityTo notExistsTaskTo = new ActivityTo(null, NOT_FOUND, ADMIN_ID, null, null, null,
                null, "epic", null, null, 4, null);
        perform(MockMvcRequestBuilders.post(ACTIVITIES_REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(notExistsTaskTo)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(ACTIVITIES_REST_URL_SLASH + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotBelong() throws Exception {
        perform(MockMvcRequestBuilders.delete(ACTIVITIES_REST_URL_SLASH + ACTIVITY1_ID))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(ACTIVITIES_REST_URL_SLASH + (ACTIVITY1_ID + 1)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(activityRepository.existsById(ACTIVITY1_ID + 1));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void deletePrimaryActivity() throws Exception {
        perform(MockMvcRequestBuilders.delete(ACTIVITIES_REST_URL_SLASH + ACTIVITY1_ID))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail", is("Primary activity cannot be delete or update with null values")));
        assertTrue(activityRepository.existsById(ACTIVITY1_ID));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getTaskAssignmentsBySprint() throws Exception {
        perform(MockMvcRequestBuilders.get(TASKS_REST_URL_SLASH + "assignments/by-sprint")
                .param(SPRINT_ID, String.valueOf(SPRINT1_ID)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_BELONG_MATCHER.contentJson(userTask1Assignment1, userTask1Assignment2,
                        userTask2Assignment1, userTask2Assignment2));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void assignToTask() throws Exception {
        perform(MockMvcRequestBuilders.patch(TASKS_REST_URL_SLASH + TASK1_ID + "/assign")
                .param(USER_TYPE, TASK_DEVELOPER))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertTrue(userBelongRepository.findActiveAssignment(TASK1_ID, TASK, ADMIN_ID, TASK_DEVELOPER).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void assignToTaskNotFound() throws Exception {
        perform(MockMvcRequestBuilders.patch(TASKS_REST_URL_SLASH + NOT_FOUND + "/assign")
                .param(USER_TYPE, TASK_DEVELOPER))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void assignToTaskWithNotPossibleUserType() throws Exception {
        perform(MockMvcRequestBuilders.patch(TASKS_REST_URL_SLASH + TASK1_ID + "/assign")
                .param(USER_TYPE, TASK_REVIEWER))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail", is(String.format(CANNOT_ASSIGN, TASK_REVIEWER, IN_PROGRESS))));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void assignToTaskTwice() throws Exception {
        assignToTask();
        assignToTask();
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void assignToTaskWhenStatusForbidAssignment() throws Exception {
        assignToTaskWhenStatusForbidAssignment(TODO_TASK_ID, TODO);
        assignToTaskWhenStatusForbidAssignment(READY_FOR_TEST_TASK_ID, READY_FOR_TEST);
        assignToTaskWhenStatusForbidAssignment(READY_FOR_REVIEW_TASK_ID, READY_FOR_REVIEW);
        assignToTaskWhenStatusForbidAssignment(DONE_TASK_ID, DONE);
        assignToTaskWhenStatusForbidAssignment(CANCELED_TASK_ID, CANCELED);
    }

    private void assignToTaskWhenStatusForbidAssignment(long taskId, String taskStatus) throws Exception {
        perform(MockMvcRequestBuilders.patch(TASKS_REST_URL_SLASH + taskId + "/assign")
                .param(USER_TYPE, TASK_DEVELOPER))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail", is(String.format(CANNOT_ASSIGN, TASK_DEVELOPER, taskStatus))));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void unAssignFromTask() throws Exception {
        perform(MockMvcRequestBuilders.patch(TASKS_REST_URL_SLASH + TASK1_ID + "/unassign")
                .param(USER_TYPE, TASK_DEVELOPER))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertTrue(userBelongRepository.findActiveAssignment(TASK1_ID, TASK, ADMIN_ID, TASK_DEVELOPER).isEmpty());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void unAssignFromTaskWithNotPossibleUserType() throws Exception {
        perform(MockMvcRequestBuilders.patch(TASKS_REST_URL_SLASH + TASK1_ID + "/unassign")
                .param(USER_TYPE, TASK_REVIEWER))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail", is(String.format(CANNOT_UN_ASSIGN, TASK_REVIEWER, IN_PROGRESS))));
        assertTrue(userBelongRepository.findActiveAssignment(TASK1_ID, TASK, ADMIN_ID, TASK_REVIEWER).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void unAssignFromTaskNotFound() throws Exception {
        perform(MockMvcRequestBuilders.patch(TASKS_REST_URL_SLASH + NOT_FOUND + "/unassign")
                .param(USER_TYPE, TASK_REVIEWER))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void unAssignFromTaskWhenStatusForbidUnAssignment() throws Exception {
        unAssignFromTaskWhenStatusForbidUnAssignment(TODO_TASK_ID, TODO);
        unAssignFromTaskWhenStatusForbidUnAssignment(READY_FOR_TEST_TASK_ID, READY_FOR_TEST);
        unAssignFromTaskWhenStatusForbidUnAssignment(READY_FOR_REVIEW_TASK_ID, READY_FOR_REVIEW);
        unAssignFromTaskWhenStatusForbidUnAssignment(DONE_TASK_ID, DONE);
        unAssignFromTaskWhenStatusForbidUnAssignment(CANCELED_TASK_ID, CANCELED);
    }

    private void unAssignFromTaskWhenStatusForbidUnAssignment(long taskId, String taskStatus) throws Exception {
        perform(MockMvcRequestBuilders.patch(TASKS_REST_URL_SLASH + taskId + "/unassign")
                .param(USER_TYPE, TASK_DEVELOPER))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail", is(String.format(CANNOT_UN_ASSIGN, TASK_DEVELOPER, taskStatus))));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void unAssignFromTaskWhenNotAssigned() throws Exception {
        perform(MockMvcRequestBuilders.patch(TASKS_REST_URL_SLASH + TASK1_ID + "/unassign")
                .param(USER_TYPE, TASK_DEVELOPER))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail", is(String
                        .format("Not found assignment with userType=%s for task {%d} for user {%d}", TASK_DEVELOPER, TASK1_ID, ADMIN_ID))));
    }
}
