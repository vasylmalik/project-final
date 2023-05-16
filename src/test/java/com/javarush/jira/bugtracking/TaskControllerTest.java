package com.javarush.jira.bugtracking;

import com.javarush.jira.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.javarush.jira.login.internal.web.UserTestData.ADMIN_MAIL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class TaskControllerTest extends AbstractControllerTest {

    private static final String REST_URL = TaskController.REST_URL;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void addNewTag() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + "/addtag")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .param("tags", "newtag")
                .param("taskId", "2"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void addNewTags() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + "/addtag")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .param("tags", "newtag1", "newtag2", "newtag3")
                .param("taskId", "2"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void addAlreadyExistsTags() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + "/addtag")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .param("tags", "newtag1", "newtag2", "newtag3")
                .param("taskId", "2"))
                .andExpect(status().isNoContent())
                .andDo(print());
        perform(MockMvcRequestBuilders.put(REST_URL + "/addtag")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .param("tags", "newtag1", "newtag2", "newtag4")
                .param("taskId", "2"))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void subscribeToTask() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + "/subscribe")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .param("taskId", "1")
                .param("userTypeCode", "admin"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

}
