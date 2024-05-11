package com.javarush.jira.profile.internal.web;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.profile.ContactTo;
import com.javarush.jira.profile.internal.ProfileRepository;
import com.javarush.jira.profile.internal.model.Contact;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;
import java.util.stream.Collectors;

import static com.javarush.jira.common.util.JsonUtil.writeValue;
import static com.javarush.jira.login.internal.web.UserTestData.ADMIN_MAIL;
import static com.javarush.jira.profile.internal.web.ProfileTestData.USER_PROFILE_TO;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProfileRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = ProfileRestController.REST_URL;

    private final int EXPECTED_ID_FOR_ADMIN = 2;

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getProfileToByUserShouldReturnProfileForAuthenticatedUser() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(EXPECTED_ID_FOR_ADMIN));
    }

    @Test
    void getUnauthenticatedShouldReturnUnauthorizedStatus() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateShouldUpdateUserProfileAndReturnSuccessfulStatus() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(USER_PROFILE_TO)))
                .andExpect(status().is2xxSuccessful());

        Set<String> actualContacts = profileRepository.getExisted(EXPECTED_ID_FOR_ADMIN)
                .getContacts().stream()
                .map(Contact::getValue)
                .collect(Collectors.toSet());

        Set<String> expectedContacts = USER_PROFILE_TO.getContacts().stream()
                .map(ContactTo::getValue)
                .collect(Collectors.toSet());

        assertTrue(actualContacts.containsAll(expectedContacts));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateShouldReturnNoContentStatusAfterSuccessfulUpdate() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(USER_PROFILE_TO)))
                .andExpect(status().isNoContent());
    }
}
