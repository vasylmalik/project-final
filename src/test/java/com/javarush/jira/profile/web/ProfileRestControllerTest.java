package com.javarush.jira.profile.web;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.profile.ContactTo;
import com.javarush.jira.profile.ProfileTo;
import com.javarush.jira.profile.internal.ProfileMapper;
import com.javarush.jira.profile.internal.ProfileRepository;
import com.javarush.jira.ref.RefType;
import com.javarush.jira.ref.ReferenceController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;

import static com.javarush.jira.common.util.JsonUtil.writeValue;
import static com.javarush.jira.login.internal.web.UserTestData.ADMIN_MAIL;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

class ProfileRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = ProfileRestController.REST_URL ;
    private final int EXPECTED_ID_FOR_ADMIN=2;

    private final ProfileTo TEST_PROFILE_FOR_UPDATING = new ProfileTo(2L,null, Set.of(new ContactTo("email","admin@gmail.com")));

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getProfileToByUser() throws Exception{
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(EXPECTED_ID_FOR_ADMIN)));

    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL ))
                .andExpect(status().isUnauthorized())
                .andDo(print())   ;
    }
    // Test doesn't pass with "IllegalArgumetException":"Value with key email not found at request /api/profile"
    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception{
                   perform(MockMvcRequestBuilders.put(REST_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(writeValue(TEST_PROFILE_FOR_UPDATING)))
                    .andExpect(status().isOk());

    }




}