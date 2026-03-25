package com.javarush.jira.profile.internal.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.profile.ContactTo;
import com.javarush.jira.profile.ProfileTo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Set;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProfileRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = "/api/profile";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "user@example.com", authorities = {"USER"})
    void getProfileShouldReturnProfileWhenAuthenticated() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mailNotifications").exists())
                .andExpect(jsonPath("$.contacts").exists());
    }

    @Test
    void getProfileShouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user@example.com", authorities = {"USER"})
    void updateProfileShouldUpdateMailNotifications() throws Exception {
        ProfileTo profileTo = new ProfileTo(null, Set.of("NEW_TASK", "TASK_COMPLETED"), null);

        mockMvc.perform(put(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileTo)))
                        .andDo(print())
                        .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user@example.com", authorities = {"USER"})
    void updateProfileShouldUpdateContacts() throws Exception {
        Set<ContactTo> contacts = Set.of(
                new ContactTo("skype", "new.skype"),
                new ContactTo("telegram", "@newuser"),
                new ContactTo("github", "newgithub")
        );

        ProfileTo profileTo = new ProfileTo(null, null, contacts);

        mockMvc.perform(put(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileTo)))
                        .andDo(print())
                        .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user@example.com", authorities = {"USER"})
    void updateProfileShouldUpdateBothMailNotificationsAndContacts() throws Exception {
        Set<String> mailNotifications = Set.of("NEW_TASK", "TASK_COMPLETED", "SPRINT_PLANNING");
        Set<ContactTo> contacts = Set.of(
                new ContactTo("skype", "test.skype"),
                new ContactTo("mobile", "+1234567890")
        );

        ProfileTo profileTo = new ProfileTo(null, mailNotifications, contacts);

        mockMvc.perform(put(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileTo)))
                        .andDo(print())
                        .andExpect(status().isNoContent());
    }
}
