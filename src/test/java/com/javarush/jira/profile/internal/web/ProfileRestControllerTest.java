package com.javarush.jira.profile.internal.web;

import com.javarush.jira.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.javarush.jira.common.util.JsonUtil.writeValue;
import static com.javarush.jira.login.internal.web.UserTestData.ADMIN_MAIL;
import static com.javarush.jira.profile.internal.web.ProfileTestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProfileRestControllerTest extends AbstractControllerTest {

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void get_withAdminUser() throws Exception {
        perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithAnonymousUser
    void get_withAnonymousUser() throws Exception {
        perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL))
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void update_withValidProfile() throws Exception {
        perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(getUpdatedTo())))
                .andExpect(status().isNoContent())
                .andDo(print());

    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void update_withInvalidProfile() throws Exception {
        perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(getInvalidTo())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

}