package com.javarush.jira.profile.internal.web;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.profile.ProfileTo;
import com.javarush.jira.profile.internal.ProfileRepository;
import com.javarush.jira.profile.internal.model.Profile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.javarush.jira.common.util.JsonUtil.writeValue;
import static com.javarush.jira.login.internal.web.UserTestData.*;
import static com.javarush.jira.profile.internal.web.ProfileTestData.PROFILE_MATCHER;
import static com.javarush.jira.profile.internal.web.ProfileTestData.TO_MATCHER;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ProfileRestControllerTest extends AbstractControllerTest {

    @Autowired
    private ProfileRepository profileRepository;

    private static final String REST_URL = ProfileRestController.REST_URL;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getProfileWithUserMailTest() throws Exception {
        ProfileTo expectedProfile = ProfileTestData.USER_PROFILE_TO;
        expectedProfile.setId(USER_ID);

        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TO_MATCHER.contentJson(expectedProfile));
    }

    @Test
    @WithUserDetails(value = GUEST_MAIL)
    void getProfileWithGuestMailTest() throws Exception {
        ProfileTo expectedProfile = ProfileTestData.GUEST_PROFILE_EMPTY_TO;
        expectedProfile.setId(GUEST_ID);

        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TO_MATCHER.contentJson(expectedProfile));
    }

    @Test
    void getProfileWithUnauthorizedUserTest() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateProfileWithUserMailTest() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(ProfileTestData.getUpdatedTo())))
                .andDo(print())
                .andExpect(status().isNoContent());

        Profile dbProfileAfter = profileRepository.getExisted(USER_ID);
        Profile updated = ProfileTestData.getUpdated(USER_ID);

        PROFILE_MATCHER.assertMatch(dbProfileAfter, updated);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateProfileWithUserMailUnknownNotificationTest() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(ProfileTestData.getWithUnknownNotificationTo())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateProfileWithUserMailInvalidUserTest() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(ProfileTestData.getInvalidTo())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateProfileWithUserMailUnknownContactTest() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(ProfileTestData.getWithUnknownContactTo())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateProfileWithUserMailHtmlUnsafeTest() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(ProfileTestData.getWithContactHtmlUnsafeTo())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}