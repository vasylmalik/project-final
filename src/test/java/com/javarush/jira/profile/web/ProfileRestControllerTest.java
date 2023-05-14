package com.javarush.jira.profile.web;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.common.util.JsonUtil;
import com.javarush.jira.login.User;
import com.javarush.jira.login.UserTo;
import com.javarush.jira.login.internal.web.UserController;
import com.javarush.jira.login.internal.web.UserTestData;
import com.javarush.jira.profile.ProfileTo;
import com.javarush.jira.profile.internal.Profile;
import com.javarush.jira.profile.internal.ProfileMapper;
import com.javarush.jira.profile.internal.ProfileRepository;
import com.javarush.jira.ref.RefType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.HashSet;
import java.util.Set;

import static com.javarush.jira.login.internal.web.UserTestData.ADMIN_MAIL;
import static com.javarush.jira.login.internal.web.UserTestData.USER_MAIL;
import static com.javarush.jira.login.internal.web.UserTestData.USER_MATCHER;
import static com.javarush.jira.profile.web.ProfileRestController.REST_URL;
import static com.javarush.jira.profile.web.ProfileTestData.DEADLINE;
import static com.javarush.jira.profile.web.ProfileTestData.ONE_DAY_BEFORE_DEADLINE;
import static com.javarush.jira.profile.web.ProfileTestData.PROFILE_TO_MATCHER;
import static com.javarush.jira.profile.web.ProfileTestData.USER_CONTACT_SKYPE;
import static com.javarush.jira.profile.web.ProfileTestData.USER_CONTACT_WEBSITE;
import static com.javarush.jira.profile.web.ProfileTestData.USER_ID;
import static com.javarush.jira.profile.web.ProfileTestData.USER_PASSWORD;
import static com.javarush.jira.profile.web.ProfileTestData.getUpdated;
import static com.javarush.jira.profile.web.ProfileTestData.jsonWithPassword;
import static com.javarush.jira.profile.web.ProfileTestData.userProfileTo;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileRestControllerTest extends AbstractControllerTest {

    @Autowired
    ProfileRepository repository;
    @Autowired
    ProfileMapper profileMapper;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(PROFILE_TO_MATCHER.contentJson(userProfileTo));
    }

    @Test
    @WithUserDetails(value = ProfileTestData.USER_MAIL)
    void update() throws Exception {
        Profile dbProfileBefore = repository.getExisted(USER_ID);
        ProfileTo dbProfileToBefore = profileMapper.toTo(dbProfileBefore);
        dbProfileToBefore.setContacts(Set.of(USER_CONTACT_SKYPE, USER_CONTACT_WEBSITE));
        dbProfileToBefore.setMailNotifications(Set.of(ONE_DAY_BEFORE_DEADLINE, DEADLINE));
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(dbProfileToBefore, USER_PASSWORD)))
                .andDo(print())
                .andExpect(status().isNoContent());
        Profile dbProfileAfter = repository.getExisted(userProfileTo.id());
        ProfileTo dbProfileToAfter = profileMapper.toTo(dbProfileAfter);
        PROFILE_TO_MATCHER.assertMatch(dbProfileToAfter, getUpdated());
    }
}
