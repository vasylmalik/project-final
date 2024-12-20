package com.javarush.jira.profile.internal.web;

import com.javarush.jira.AbstractControllerTest;

import com.javarush.jira.common.util.JsonUtil;
import com.javarush.jira.profile.ProfileDTO;
import com.javarush.jira.profile.internal.ProfileMapper;
import com.javarush.jira.profile.internal.ProfileRepository;
import com.javarush.jira.profile.internal.model.Profile;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static com.javarush.jira.login.internal.web.UserTestData.ADMIN_MAIL;
import static com.javarush.jira.login.internal.web.UserTestData.USER_MAIL;
import static com.javarush.jira.profile.internal.web.ProfileRestController.REST_URL;
import static com.javarush.jira.profile.internal.web.ProfileTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileRestControllerTest extends AbstractControllerTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileMapper profileMapper;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void shouldSucceeded_WhenUserAuthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(PROFILE_DTO_MATCHER.contentJson(USER_PROFILE_DTO));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void shouldSucceeded_WhenAdminAuthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(PROFILE_DTO_MATCHER.contentJson(ADMIN_PROFILE_DTO));
    }

    @Test
    void shouldFail_WhenUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    @WithUserDetails(value = USER_MAIL)
    void shouldSucceeded_WhenUpdateWithUserProfile() throws Exception {
        Profile userProfileToUpdate = profileRepository.getExisted(USER_ID);
        ProfileDTO userProfileDTOtoUpdate = profileMapper.toDTO(userProfileToUpdate);

        userProfileDTOtoUpdate.setMailNotifications(USER_NOTIFICATIONS_UPDATED);
        userProfileDTOtoUpdate.setContacts(USER_CONTACTS_UPDATED);

        perform(
                MockMvcRequestBuilders.put(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(userProfileDTOtoUpdate)))
                .andDo(print())
                .andExpect(status().isNoContent());

        Profile userDbProfileAfterUpdate = profileRepository.getExisted(USER_ID);
        ProfileDTO userDbProfileDTOAfterUpdate = profileMapper.toDTO(userDbProfileAfterUpdate);

        assertEquals(userProfileDTOtoUpdate, userDbProfileDTOAfterUpdate);
    }

    // alternative
    @Test
    @Transactional
    @WithUserDetails(value = ADMIN_MAIL)
    void shouldSucceeded_WhenUpdateWithAdminProfile() throws Exception {
        ProfileDTO adminProfileDTOtoUpdate = ProfileTestData.getUpdatedAdminDTO();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(adminProfileDTOtoUpdate)))
                .andDo(print())
                .andExpect(status().isNoContent());

        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(PROFILE_DTO_MATCHER.contentJson(adminProfileDTOtoUpdate));
    }

    @Test
    @Transactional
    @WithUserDetails(value = ADMIN_MAIL)
    void shouldFail_WhenUpdateWithInvalidDTO() throws Exception {
        ProfileDTO invalidDTO = getInvalidDTO();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalidDTO)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional
    @WithUserDetails(value = ADMIN_MAIL)
    void shouldFail_WhenUpdateWithUnknownNotificationDTO() throws Exception {
        ProfileDTO unknownNotificationDTO = getWithUnknownNotificationDTO();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(unknownNotificationDTO)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional
    @WithUserDetails(value = ADMIN_MAIL)
    void shouldFail_WhenUpdateWithUnknownContactDTO() throws Exception {
        ProfileDTO unknownContactDTO = getWithUnknownContactDTO();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(unknownContactDTO)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    @Transactional
    void shouldFail_WhenUpdateWithContactHtmlUnsafeDTO() throws Exception {
        ProfileDTO contactHtmlUnsafeDTO = getWithContactHtmlUnsafeDTO();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(contactHtmlUnsafeDTO)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}
