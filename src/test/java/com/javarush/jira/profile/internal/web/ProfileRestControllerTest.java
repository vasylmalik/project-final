package com.javarush.jira.profile.internal.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.profile.internal.ProfileMapper;
import com.javarush.jira.profile.internal.ProfileRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.javarush.jira.login.internal.web.UserTestData.*;
import static com.javarush.jira.profile.internal.web.ProfileTestData.USER_PROFILE_TO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileRestControllerTest extends AbstractControllerTest {
    public static final String REST_URL = "/api/profile";
    @Autowired
    protected ProfileMapper profileMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProfileRepository profileRepository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    @DisplayName("Test get user profile with auth user functionality")
    void givenAuthUser_whenGet_thenSuccessResponse() throws Exception {
        //given
        //when
        ResultActions result = perform(get(REST_URL));
        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mailNotifications.size()", CoreMatchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.contacts.size()", CoreMatchers.is(3)));
    }

    @Test
    @DisplayName("Test get user profile with not auth user functionality")
    void givenNotAuthUser_whenGet_thenErrorResponse() throws Exception {
        //given
        //when
        ResultActions result = perform(get(REST_URL));
        //then
        result
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(401)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail", CoreMatchers.is("Full authentication is required to access this resource")));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    @DisplayName("Test update user profile with auth user functionality")
    void givenProfileToAndAuthUser_whenUpdate_thenSuccessResponse() throws Exception {
        //given
        //when
        ResultActions result = perform(put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(USER_PROFILE_TO)));

        //then
        result
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Test update user profile with not auth user functionality")
    void givenProfileToAndNotAuthUser_whenUpdate_thenErrorResponse() throws Exception {
        //given
        //when
        ResultActions result = perform(put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(USER_PROFILE_TO)));

        //then
        result
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(401)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail", CoreMatchers.is("Full authentication is required to access this resource")));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    @DisplayName("Test update user profile with auth user and invalid profile functionality")
    void givenInvalidToToAndAuthUser_whenUpdate_thenErrorResponse() throws Exception {
        //given
        //when
        ResultActions result = perform(put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(ProfileTestData.getInvalidTo())));

        //then
        result
                .andDo(print())
                .andExpect(status().is(422));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    @DisplayName("Test update user profile with auth user and unknown contact functionality")
    void givenUnknownContactToAndAuthUser_whenUpdate_thenErrorResponse() throws Exception {
        //given
        //when
        ResultActions result = perform(put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(ProfileTestData.getWithUnknownContactTo())));

        //then
        result
                .andDo(print())
                .andExpect(status().is(422));
    }
}