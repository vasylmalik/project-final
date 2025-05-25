package com.javarush.jira.profile.internal.web;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.profile.ContactTo;
import com.javarush.jira.profile.ProfileTo;
import com.javarush.jira.profile.internal.ProfileRepository;
import com.javarush.jira.profile.internal.model.Profile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Set;

import static com.javarush.jira.profile.internal.web.ProfileTestData.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



class ProfileRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = "/api/profile";

    @Autowired
    private ProfileRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    @WithUserDetails(value = "admin@gmail.com")
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(is(2)))
                .andExpect(jsonPath("$.mailNotifications").value(hasSize(3)))
                .andExpect(jsonPath("$.mailNotifications").value(containsInAnyOrder(
                        "three_days_before_deadline",
                        "two_days_before_deadline",
                        "one_day_before_deadline")))
                .andExpect(jsonPath("$.contacts").value(hasSize(3)))
                .andExpect(jsonPath("$.contacts[*].code").value(containsInAnyOrder("github", "tg", "vk")))
                .andExpect(jsonPath("$.contacts[*].value").value(containsInAnyOrder("adminGitHub", "adminTg", "adminVk")))
                .andExpect(jsonPath("$.lastLogin").doesNotExist());
    }

    @Test
    @Transactional
    @WithUserDetails(value = "guest@gmail.com")
    void getNonExistingProfile() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(is(3)))
                .andExpect(jsonPath("$.mailNotifications").value(hasSize(0)))
                .andExpect(jsonPath("$.contacts").value(hasSize(0)))
                .andExpect(jsonPath("$.lastLogin").doesNotExist());
    }

    @Test
    @Transactional
    @WithUserDetails(value = "user@gmail.com")
    void getAnotherExistingProfile() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(is(1)))
                .andExpect(jsonPath("$.mailNotifications").value(hasSize(3)))
                .andExpect(jsonPath("$.mailNotifications").value(containsInAnyOrder(
                        "overdue",
                        "assigned",
                        "deadline")))
                .andExpect(jsonPath("$.contacts").value(hasSize(3)))
                .andExpect(jsonPath("$.contacts[*].code").value(containsInAnyOrder("skype", "mobile", "website")))
                .andExpect(jsonPath("$.contacts[*].value").value(containsInAnyOrder("userSkype", "+01234567890", "user.com")))
                .andExpect(jsonPath("$.lastLogin").doesNotExist());
    }

    @Test
    @Transactional
    @WithUserDetails(value = "manager@gmail.com")
    void getAnotherNonExistingProfile() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(is(4)))
                .andExpect(jsonPath("$.mailNotifications").value(hasSize(0)))
                .andExpect(jsonPath("$.contacts").value(hasSize(0)))
                .andExpect(jsonPath("$.lastLogin").doesNotExist());
    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin@gmail.com")
    void update() throws Exception {
        ProfileTo profileTo = getNewTo();
        profileTo.setId(2L);
        profileTo.setMailNotifications(Set.of("three_days_before_deadline", "two_days_before_deadline", "one_day_before_deadline"));
        profileTo.setContacts(Set.of(
                new ContactTo("github", "adminGitHub"),
                new ContactTo("tg", "adminTg"),
                new ContactTo("vk", "adminVk")
        ));

        perform(MockMvcRequestBuilders.put(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileTo)))
                .andExpect(status().isNoContent());

        Profile profile = repository.findById(2L).orElseThrow();
        Assertions.assertEquals(14L, profile.getMailNotifications());
        Assertions.assertEquals(3, profile.getContacts().size());
        Assertions.assertTrue(profile.getContacts().stream().anyMatch(c -> c.getCode().equals("github") && c.getValue().equals("adminGitHub")));
        Assertions.assertTrue(profile.getContacts().stream().anyMatch(c -> c.getCode().equals("tg") && c.getValue().equals("adminTg")));
        Assertions.assertTrue(profile.getContacts().stream().anyMatch(c -> c.getCode().equals("vk") && c.getValue().equals("adminVk")));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "guest@gmail.com")
    void updateNonExistingProfile() throws Exception {
        ProfileTo profileTo = getNewTo();
        profileTo.setId(3L);
        profileTo.setContacts(Set.of(
                new ContactTo("skype", "guestSkype")
        ));

        perform(MockMvcRequestBuilders.put(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileTo)))
                .andExpect(status().isNoContent());

        Profile profile = repository.findById(3L).orElseThrow();
        Assertions.assertEquals(14L, profile.getMailNotifications());
        Assertions.assertEquals(1, profile.getContacts().size());
        Assertions.assertTrue(profile.getContacts().stream().anyMatch(c -> c.getCode().equals("skype") && c.getValue().equals("guestSkype")));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin@gmail.com")
    void updateWithWrongId() throws Exception {
        ProfileTo profileTo = getNewTo();
        profileTo.setId(999L); // Несоответствует authUser.id=2
        profileTo.setMailNotifications(Set.of("new_task"));
        profileTo.setContacts(Set.of(new ContactTo("tg", "wrongTg")));

        perform(MockMvcRequestBuilders.put(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileTo)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin@gmail.com")
    void updateWithInvalidContactCode() throws Exception {
        ProfileTo profileTo = getNewTo();
        profileTo.setId(2L);
        profileTo.setMailNotifications(Set.of("new_task"));
        profileTo.setContacts(Set.of(new ContactTo("invalid_code", "invalidValue")));

        perform(MockMvcRequestBuilders.put(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileTo)))
                .andExpect(status().isUnprocessableEntity());
    }
}