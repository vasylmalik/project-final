package com.javarush.jira.profile.internal.web;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.common.util.JsonUtil;
import com.javarush.jira.login.AuthUser;
import com.javarush.jira.profile.ContactTo;
import com.javarush.jira.profile.ProfileTo;
import com.javarush.jira.profile.internal.ProfileMapper;
import com.javarush.jira.profile.internal.ProfileRepository;
import com.javarush.jira.profile.internal.model.Profile;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;

import static com.javarush.jira.login.internal.web.UserTestData.USER_MAIL;
import static com.javarush.jira.login.internal.web.UserTestData.user;
import static com.javarush.jira.profile.internal.web.ProfileTestData.USER_PROFILE_TO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ProfileRestControllerTest extends AbstractControllerTest {

    @MockBean
    private ProfileRepository profileRepository;

    @MockBean
    private ProfileMapper profileMapper;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get_Authenticated_ReturnsProfile() throws Exception {
        AuthUser authUser = new AuthUser(user);
        Long userId = user.getId();

        Profile mockProfile = new Profile(userId);

        when(profileRepository.getOrCreate(userId)).thenReturn(mockProfile);
        when(profileMapper.toTo(mockProfile)).thenReturn(USER_PROFILE_TO);

        perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL)
                .with(user(authUser))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mailNotifications").isArray())
                .andExpect(jsonPath("$.contacts").isArray());
    }

    @Test
    void get_Unauthenticated_ReturnsUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void update_Authenticated_UpdatesProfile() throws Exception {
        AuthUser authUser = new AuthUser(user);
        Long userId = user.getId();

        ProfileTo updatedTo = new ProfileTo(userId, Set.of(), Set.of());

        Profile mockProfile = new Profile(userId);
        when(profileRepository.getOrCreate(userId)).thenReturn(mockProfile);
        when(profileMapper.updateFromTo(any(Profile.class), any(ProfileTo.class))).thenReturn(mockProfile);
        when(profileRepository.save(any(Profile.class))).thenReturn(mockProfile);

        perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
                .with(user(authUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andExpect(status().isNoContent());
    }

    @Test
    @org.springframework.security.test.context.support.WithUserDetails(value = com.javarush.jira.login.internal.web.UserTestData.USER_MAIL)
    void update_InvalidData_ReturnsBadRequestOrUnprocessable() throws Exception {
        ProfileTo invalidTo = new ProfileTo(1L, Set.of(""), Set.of(new ContactTo("", "")));

        perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalidTo)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void update_Unauthenticated_ReturnsUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isUnauthorized());
    }
}