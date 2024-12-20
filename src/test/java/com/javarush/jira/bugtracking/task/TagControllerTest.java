package com.javarush.jira.bugtracking.task;

import com.javarush.jira.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static com.javarush.jira.bugtracking.task.TaskTestData.*;
import static com.javarush.jira.common.util.JsonUtil.writeValue;
import static com.javarush.jira.login.internal.web.UserTestData.ADMIN_MAIL;
import static com.javarush.jira.bugtracking.task.TaskController.REST_URL;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TagControllerTest extends AbstractControllerTest {

    @Autowired
    private TaskRepository taskRepository;

    private static final String TASKS_REST_URL_SLASH_WITH_ID = REST_URL + "/" + TASK1_ID;

    private static final String TASK_TAGS_GET =  TASKS_REST_URL_SLASH_WITH_ID + "/get-tags";
    private static final String TASK_TAGS_ADD =  TASKS_REST_URL_SLASH_WITH_ID + "/add-tags";
    private static final String TASK_TAGS_REPLACE =  TASKS_REST_URL_SLASH_WITH_ID + "/replace-tags";
    private static final String TASK_TAGS_DELETE =  TASKS_REST_URL_SLASH_WITH_ID + "/delete-tags";
    private static final String TASK_TAGS_DELETE_SINGLE =  TASKS_REST_URL_SLASH_WITH_ID + "/delete-single-tag";

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void shouldSucceeded_WhenGetTagsByTaskId() throws Exception {
        perform(MockMvcRequestBuilders.get(TASK_TAGS_GET))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        Task task = taskRepository.getTaskByIdWithTags(TASK1_ID);
        assertEquals(tags_data, task.getTags());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    @Transactional
    void shouldSucceeded_WhenAddTagsToTask() throws Exception {
        perform(MockMvcRequestBuilders.put(TASK_TAGS_ADD)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(writeValue(tags_data_to_add)))
                .andDo(print())
                .andExpect(status().isCreated());

        Task task_after_add = taskRepository.getTaskByIdWithTags(TASK1_ID);
        assertEquals(tags_data_after_add, task_after_add.getTags());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    @Transactional
    void shouldSucceeded_WhenReplaceTags() throws Exception {
        perform(MockMvcRequestBuilders.post(TASK_TAGS_REPLACE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(tags_for_replace)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        Task task_after_update = taskRepository.getTaskByIdWithTags(TASK1_ID);
        assertEquals(tags_for_replace,task_after_update.getTags());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    @Transactional
    void shouldSucceeded_WhenDeleteTags() throws Exception {
        Task task_before_delete = taskRepository.getTaskByIdWithTags(TASK1_ID);
        assertFalse(task_before_delete.getTags().isEmpty());


        perform(MockMvcRequestBuilders.delete(TASK_TAGS_DELETE))
                .andDo(print())
                .andExpect(status().isNoContent());

        Task task_after_delete = taskRepository.getTaskByIdWithTags(TASK1_ID);
        assertTrue(task_after_delete.getTags().isEmpty());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    @Transactional
    void deleteSingleTag() throws Exception {
        Task task_before_delete_single = taskRepository.getTaskByIdWithTags(TASK1_ID);
        assertFalse(task_before_delete_single.getTags().isEmpty());

        perform(MockMvcRequestBuilders.delete(TASK_TAGS_DELETE_SINGLE)
                    .content(tag_for_single_delete)
                    .contentType(MediaType.TEXT_PLAIN))
                .andDo(print())
                .andExpect(status().isOk());

        Task task_after_delete_single = taskRepository.getTaskByIdWithTags(TASK1_ID);
        assertEquals(tags_after_single_delete, task_after_delete_single.getTags());
    }
}