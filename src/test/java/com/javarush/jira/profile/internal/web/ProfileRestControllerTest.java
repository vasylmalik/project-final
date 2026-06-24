package com.javarush.jira.profile.internal.web;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.profile.ContactTo;
import com.javarush.jira.profile.ProfileTo;
import com.javarush.jira.profile.internal.ProfileRepository;
import com.javarush.jira.profile.internal.model.Profile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.Set;

import static com.javarush.jira.common.util.JsonUtil.writeValue;
import static com.javarush.jira.profile.internal.web.ProfileTestData.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProfileRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = ProfileRestController.REST_URL;

    @Autowired
    private ProfileRepository profileRepository;

    // ==================== GET ====================

    @Test
    @WithUserDetails(value = "user@gmail.com")
    void getProfile_success() throws Exception {
        perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.contacts").isArray())
                .andExpect(jsonPath("$.contacts[?(@.code=='skype')].value").value("userSkype"))
                .andExpect(jsonPath("$.mailNotifications").isArray())
                .andExpect(jsonPath("$.mailNotifications", containsInAnyOrder(USER_PROFILE_TO.getMailNotifications().toArray()
                )));
    }

    @Test
    @WithAnonymousUser
    void getProfile_unauthorized() throws Exception {
        perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    // ==================== PUT ====================

    @Test
    @WithUserDetails(value = "user@gmail.com")
    void updateProfile_success() throws Exception {
        ProfileTo updateTo = getUpdatedTo();
        String json = writeValue(updateTo);

        perform(put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isNoContent());

        Profile expected = getUpdated(1L);
        Profile actual = profileRepository.findById(1L).orElseThrow();

        PROFILE_MATCHER.assertMatch(actual, expected);
    }

    @Test
    @WithAnonymousUser
    void updateProfile_unauthorized() throws Exception {
        ProfileTo updateTo = new ProfileTo(null, Set.of("assigned"), Set.of(new ContactTo("skype", "test")));
        String json = writeValue(updateTo);

        perform(put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithUserDetails(value = "user@gmail.com")
    void updateProfile_emptyContactsAndNotifications() throws Exception {
        ProfileTo updateTo = GUEST_PROFILE_EMPTY_TO;
        String json = writeValue(updateTo);

        perform(put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isNoContent());

        Profile expected = new Profile(1L);
        expected.setMailNotifications(0);
        expected.setContacts(Set.of());

        Profile actual = profileRepository.findById(1L).orElseThrow();
        PROFILE_MATCHER.assertMatch(actual, expected);
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    void updateProfile_invalidContactCode() throws Exception {
        ProfileTo invalidTo = getWithUnknownContactTo();
        String json = writeValue(invalidTo);

        perform(put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.detail").value(containsString("Value with key WrongContactCode not found")));
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    void updateProfile_invalidContactValueEmpty() throws Exception {
        ProfileTo invalidTo = getInvalidTo();
        String json = writeValue(invalidTo);

        perform(put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.detail").value(containsString("must not be blank")));
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    void updateProfile_invalidMailNotificationCode() throws Exception {
        ProfileTo invalidTo = getWithUnknownNotificationTo();
        String json = writeValue(invalidTo);

        perform(put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.detail").value(containsString("Value with key WrongNotification not found")));
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    void updateProfile_invalidJson() throws Exception {
        perform(put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid}"))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    void updateProfile_contactHtmlUnsafe() throws Exception {
        ProfileTo invalidTo = getWithContactHtmlUnsafeTo();
        String json = writeValue(invalidTo);

        perform(put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.detail").value(containsString("{error.noHtml}")));
    }
}