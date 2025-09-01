package com.javarush.jira.profile.internal.web;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.common.BaseHandler;
import com.javarush.jira.common.util.JsonUtil;
import com.javarush.jira.login.internal.web.UserTestData;
import com.javarush.jira.profile.ProfileTo;
import com.javarush.jira.profile.internal.ProfileRepository;
import com.javarush.jira.profile.internal.model.Profile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.stream.Stream;

import static com.javarush.jira.profile.internal.web.ProfileTestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ProfileRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL_PROFILE = BaseHandler.REST_URL + "/profile";

    //    @MockBean
    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    public void get() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(REST_URL_PROFILE);

        perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(PROFILE_TO_MATCHER.contentJson(USER_PROFILE_TO));
    }

    @Test
    @WithUserDetails(value = UserTestData.GUEST_MAIL)
    public void get_whenUserIsGuest() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(REST_URL_PROFILE);

        perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(PROFILE_TO_MATCHER.contentJson(GUEST_PROFILE_EMPTY_TO));
    }

    @Test
    public void get_whenUnauthorizedUser() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(REST_URL_PROFILE);
        perform(requestBuilder)
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    public void update() throws Exception {
        long id = 1L;

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(REST_URL_PROFILE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JsonUtil.writeValue(ProfileTestData.getUpdatedTo(id)));

        perform(requestBuilder).andExpect(status().isNoContent());

        Profile expect = ProfileTestData.getUpdated(id);
        Profile actual = profileRepository.findById(id).orElseThrow();
        PROFILE_MATCHER.assertMatch(actual, expect);
    }

    @Test
    @WithUserDetails(value = UserTestData.MANAGER_MAIL)
    public void update_whenNew() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(REST_URL_PROFILE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JsonUtil.writeValue(ProfileTestData.getNewTo()));

        perform(requestBuilder).andExpect(status().isNoContent());

        long id = 4L;
        Profile expect = ProfileTestData.getNew(id);
        Profile actual = profileRepository.getExisted(4L);
        PROFILE_MATCHER.assertMatch(actual, expect);
    }

    @ParameterizedTest
    @MethodSource(value = "whenInvalidObjParams")
    @WithUserDetails(value = UserTestData.MANAGER_MAIL)
    public void update_whenInvalidObject(ProfileTo profileTo) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(REST_URL_PROFILE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JsonUtil.writeValue(profileTo));

        perform(requestBuilder).andExpect(status().is4xxClientError());
    }

    public static Stream<Arguments> whenInvalidObjParams() {
        return Stream.of(
                Arguments.of(ProfileTestData.getInvalidTo()),
                Arguments.of(ProfileTestData.getWithUnknownNotificationTo()),
                Arguments.of(ProfileTestData.getWithUnknownContactTo()),
                Arguments.of(ProfileTestData.getWithContactHtmlUnsafeTo())
        );
    }
}