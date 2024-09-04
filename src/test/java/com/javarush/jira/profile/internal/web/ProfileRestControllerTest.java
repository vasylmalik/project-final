package com.javarush.jira.profile.internal.web;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.common.util.JsonUtil;
import com.javarush.jira.login.internal.web.UserTestData;
import com.javarush.jira.profile.ProfileTo;
import com.javarush.jira.profile.internal.ProfileRepository;
import com.javarush.jira.profile.internal.model.Profile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ProfileRestControllerTest extends AbstractControllerTest {
    private final ProfileRepository profileRepository;
    @Autowired
    ProfileRestControllerTest(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }
    @Test
    @WithUserDetails(value = UserTestData.GUEST_MAIL)
    void getGuest() throws Exception {
        ProfileTestData.GUEST_PROFILE_EMPTY_TO.setId(UserTestData.GUEST_ID);
        perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getUser() throws Exception {
        ProfileTestData.USER_PROFILE_TO.setId(UserTestData.USER_ID);
        perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL))
                .andExpect(status().isUnauthorized());
    }
    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateTask() throws Exception {
        ProfileTo profileTo = ProfileTestData.getUpdatedTo();
        perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(profileTo)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent());
        Profile profile = ProfileTestData.getUpdated(UserTestData.USER_ID);
        ProfileTestData.PROFILE_MATCHER.assertMatch(profileRepository.getExisted(UserTestData.USER_ID), profile);
    }
    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateTaskInvalid() throws Exception {
        ProfileTo profileTo = ProfileTestData.getInvalidTo();
        perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(profileTo)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnprocessableEntity());
    }
    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateTaskUnknownContact() throws Exception {
    ProfileTo profileTo = ProfileTestData.getWithUnknownContactTo();
        perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(profileTo)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnprocessableEntity());
    }
    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateTaskUnknownNotification() throws Exception {
        ProfileTo profileTo = ProfileTestData.getWithUnknownNotificationTo();
        perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(profileTo)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnprocessableEntity());
    }
    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateTaskContactHtmlUnsafe() throws Exception {
        ProfileTo profileTo = ProfileTestData.getWithContactHtmlUnsafeTo();
        perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(profileTo)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnprocessableEntity());
    }
}