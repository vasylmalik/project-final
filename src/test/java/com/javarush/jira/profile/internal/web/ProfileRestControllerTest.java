package com.javarush.jira.profile.internal.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.jira.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static com.javarush.jira.login.internal.web.UserTestData.*;
import static com.javarush.jira.profile.internal.web.ProfileTestData.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProfileRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = ProfileRestController.REST_URL;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void get_shouldReturnUserProfileTo_whenAuthenticatedAsUser() throws Exception {
        perform(get(REST_URL)
                .with(httpBasic(USER_MAIL, "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(PROFILE_TO_MATCHER.contentJson(USER_PROFILE_TO));
    }

    @Test
    void get_shouldReturnEmptyProfileTo_whenAuthenticatedAsGuest() throws Exception {
        perform(get(REST_URL)
                .with(httpBasic(GUEST_MAIL, "guest")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(PROFILE_TO_MATCHER.contentJson(GUEST_PROFILE_EMPTY_TO));
    }

    @Test
    void get_shouldReturn401_whenNotAuthenticated() throws Exception {
        perform(get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void update_shouldReturn204_whenValidData() throws Exception {
        perform(put(REST_URL)
                .with(httpBasic(USER_MAIL, "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUpdatedTo())))
                .andExpect(status().isNoContent());
    }

    @Test
    void update_shouldReturn401_whenNotAuthenticated() throws Exception {
        perform(put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUpdatedTo())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void update_shouldReturn422_whenInvalidData() throws Exception {
        perform(put(REST_URL)
                .with(httpBasic(USER_MAIL, "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getInvalidTo())))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void update_shouldReturn422_whenUnknownNotification() throws Exception {
        perform(put(REST_URL)
                .with(httpBasic(USER_MAIL, "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getWithUnknownNotificationTo())))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void update_shouldReturn422_whenUnknownContact() throws Exception {
        perform(put(REST_URL)
                .with(httpBasic(USER_MAIL, "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getWithUnknownContactTo())))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void update_shouldReturn422_whenContactValueContainsHtmlUnsafeContent() throws Exception {
        perform(put(REST_URL)
                .with(httpBasic(USER_MAIL, "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getWithContactHtmlUnsafeTo())))
                .andExpect(status().isUnprocessableEntity());
    }
}