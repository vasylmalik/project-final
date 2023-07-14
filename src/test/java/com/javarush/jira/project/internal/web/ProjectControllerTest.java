package com.javarush.jira.project.internal.web;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.bugtracking.project.Project;
import com.javarush.jira.bugtracking.project.ProjectRepository;
import com.javarush.jira.common.BaseHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.javarush.jira.common.util.JsonUtil.writeValue;
import static com.javarush.jira.login.internal.web.UserTestData.MANAGER_MAIL;
import static com.javarush.jira.login.internal.web.UserTestData.USER_MAIL;
import static com.javarush.jira.project.internal.web.ProjectTestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProjectControllerTest extends AbstractControllerTest {
    private static final String REST_URL_PROJECT = BaseHandler.REST_URL + "/projects";
    private static final String REST_URL_MNGR_PROJECT = BaseHandler.REST_URL + "/mngr/projects";

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_PROJECT + "/" + PARENT_PROJECT_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(PROJECT_TO_MATCHER.contentJson(projectTo1));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_PROJECT))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = MANAGER_MAIL)
    void update() throws Exception {
        projectTo2.setTitle("PROJECT-2 UPD");
        perform(MockMvcRequestBuilders.put(REST_URL_MNGR_PROJECT + "/" + PROJECT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(projectTo2)))
                .andDo(print())
                .andExpect(status().isNoContent());
        PROJECT_TO_MATCHER.assertMatch(projectTo2, ProjectTestData.getUpdated());
    }

    @Test
    @WithUserDetails(value = MANAGER_MAIL)
    void createNoBody() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL_MNGR_PROJECT)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = MANAGER_MAIL)
    void create() throws Exception {
        Project newProject = ProjectTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL_MNGR_PROJECT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newProject)))
                .andExpect(status().isCreated());
        Project created = PROJECT_MATCHER.readFromJson(action);
        PROJECT_MATCHER.assertMatch(created, newProject);
        PROJECT_MATCHER.assertMatch(projectRepository.getExisted(created.id()), newProject);
    }

    @Test
    @WithUserDetails(value = MANAGER_MAIL)
    void disable() throws Exception {
        projectTo2.setEnabled(false);
        perform(MockMvcRequestBuilders.put(REST_URL_MNGR_PROJECT + "/" + PROJECT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(projectTo2)))
                .andDo(print())
                .andExpect(status().isNoContent());
        PROJECT_TO_MATCHER.assertMatch(projectTo2, ProjectTestData.getDisabled());
    }
}

