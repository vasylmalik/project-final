package com.javarush.jira.profile.internal.web;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.common.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.javarush.jira.common.util.JsonUtil.writeValue;
import static com.javarush.jira.login.internal.web.UserTestData.*;
import static com.javarush.jira.profile.internal.web.ProfileTestData.USER_PROFILE_TO;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ProfileRestControllerTest extends AbstractControllerTest {
    private static final String API_URL = ProfileRestController.REST_URL;

    @Test
    public void unauthorizedUserCannotAccessProfile() throws Exception {
        perform(MockMvcRequestBuilders.get(API_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    public void adminCanAccessProfile() throws Exception {
        perform(MockMvcRequestBuilders.get(API_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithUserDetails(value = GUEST_MAIL)
    public void guestCanAccessProfile() throws Exception {
        perform(MockMvcRequestBuilders.get(API_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    public void userCanAccessProfile() throws Exception {
        perform(MockMvcRequestBuilders.get(API_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void unauthorizedUserCannotUpdateProfile() throws Exception {
        perform(MockMvcRequestBuilders.put(API_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void adminCanUpdate() throws Exception {
        perform(MockMvcRequestBuilders.put(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(USER_PROFILE_TO)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithUserDetails(value = GUEST_MAIL)
    void guestCanUpdate() throws Exception {
        perform(MockMvcRequestBuilders.put(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(USER_PROFILE_TO)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void userCanUpdate() throws Exception {
        perform(MockMvcRequestBuilders.put(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(USER_PROFILE_TO)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void sendInvalidData_serverMustReturnStatusError() throws Exception {
        perform(MockMvcRequestBuilders.put(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(ProfileTestData.getInvalidTo())))
                .andExpect(status().is4xxClientError());
    }
}