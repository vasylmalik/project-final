package com.javarush.jira.bugtracking;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.bugtracking.internal.model.Task;
import com.javarush.jira.bugtracking.internal.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static com.javarush.jira.login.internal.web.UserTestData.ADMIN_MAIL;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TaskControllerTest extends AbstractControllerTest {
    private static final String REST_URL = TaskController.REST_URL;

        @Test
    @WithUserDetails(value = ADMIN_MAIL)
    public void shouldGetStatus404WhenTaskIsNotFound() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + "/{taskId}/addTags", 6)
                .param("tags", "tag1")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    public void shouldGetStatus204WhenIsOk() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + "/{taskId}/addTags", 2)
                .param("tags", "tag2")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    public void shouldGetStatus400WhenPathVariableIsEmpty() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + "/{taskId}/addTags")
                .param("tags", "tag3")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    public void shouldGetStatus400WhenRequestParamIsEmpty() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + "/{taskId}/addTags", 2)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}