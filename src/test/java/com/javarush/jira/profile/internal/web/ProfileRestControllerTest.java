package com.javarush.jira.profile.internal.web;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.profile.ProfileTo;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.javarush.jira.login.internal.web.UserTestData.USER_MAIL;
import static com.javarush.jira.profile.internal.web.ProfileRestController.REST_URL;
import static com.javarush.jira.profile.internal.web.ProfileTestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class ProfileRestControllerTest extends AbstractControllerTest {

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mailNotifications").isArray())
                .andExpect(jsonPath("$.contacts").isArray());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void update() throws Exception {
        ProfileTo updatedTo = getUpdatedTo();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithContacts(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateInvalid() throws Exception {
        ProfileTo invalidTo = getInvalidTo();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithContacts(invalidTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateWithUnknownNotification() throws Exception {
        ProfileTo invalidTo = getWithUnknownNotificationTo();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithContacts(invalidTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateWithUnknownContact() throws Exception {
        ProfileTo invalidTo = getWithUnknownContactTo();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithContacts(invalidTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateWithHtmlUnsafeContact() throws Exception {
        ProfileTo invalidTo = getWithContactHtmlUnsafeTo();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithContacts(invalidTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateUnAuth() throws Exception {
        ProfileTo updatedTo = getUpdatedTo();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithContacts(updatedTo)))
                .andExpect(status().isUnauthorized());
    }

    private String jsonWithContacts(ProfileTo profileTo) {
        StringBuilder json = new StringBuilder();
        json.append("{");

        if (profileTo.getId() != null) {
            json.append("\"id\":").append(profileTo.getId()).append(",");
        }

        if (profileTo.getMailNotifications() != null) {
            json.append("\"mailNotifications\":[");
            json.append(String.join(",",
                    profileTo.getMailNotifications().stream()
                            .map(s -> "\"" + s + "\"")
                            .toArray(String[]::new)));
            json.append("],");
        }

        if (profileTo.getContacts() != null) {
            json.append("\"contacts\":[");
            json.append(profileTo.getContacts().stream()
                    .map(c -> String.format("{\"code\":\"%s\",\"value\":\"%s\"}",
                            c.getCode(), c.getValue()))
                    .reduce((a, b) -> a + "," + b)
                    .orElse(""));
            json.append("]");
        }

        String result = json.toString();
        if (result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }
        result += "}";
        return result;
    }
}