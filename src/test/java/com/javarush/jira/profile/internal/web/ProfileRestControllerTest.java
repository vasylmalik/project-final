package com.javarush.jira.profile.internal.web;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.common.BaseHandler;
import com.javarush.jira.profile.ProfileTo;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.javarush.jira.common.util.JsonUtil.writeValue;
import static com.javarush.jira.login.internal.web.UserTestData.USER_MAIL;
import static com.javarush.jira.profile.internal.web.ProfileTestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL_PROFILE = BaseHandler.REST_URL + "/profile";

    @Test
    void getProfileUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_PROFILE))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getProfile() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_PROFILE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(PROFILE_TO_MATCHER.contentJson(USER_PROFILE_TO));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateProfile() throws Exception {
        ProfileTo updatedTo = getUpdatedTo();
        perform(MockMvcRequestBuilders.put(REST_URL_PROFILE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        perform(MockMvcRequestBuilders.get(REST_URL_PROFILE))
                .andExpect(status().isOk())
                .andExpect(PROFILE_TO_MATCHER.contentJson(updatedTo));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateProfileWithNoBody() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL_PROFILE)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateProfileWithUnknownContactTo() throws Exception {
        ProfileTo profileToWithUnknownContactTo = getWithUnknownContactTo();

        perform(MockMvcRequestBuilders.put(REST_URL_PROFILE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(profileToWithUnknownContactTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateProfileWithUnknownNotificationTo() throws Exception {
        ProfileTo profileToWithUnknownNotificationTo = getWithUnknownNotificationTo();

        perform(MockMvcRequestBuilders.put(REST_URL_PROFILE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(profileToWithUnknownNotificationTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateProfileWithInvalidData() throws Exception {
        ProfileTo invalidTo = getInvalidTo();

        perform(MockMvcRequestBuilders.put(REST_URL_PROFILE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(invalidTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}