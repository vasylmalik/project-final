package com.javarush.jira.profile.internal.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.jira.login.AuthUser;
import com.javarush.jira.login.Role;
import com.javarush.jira.login.User;
import com.javarush.jira.profile.ContactTo;
import com.javarush.jira.profile.ProfileTo;
import com.javarush.jira.profile.internal.ProfileMapper;
import com.javarush.jira.profile.internal.ProfileRepository;
import com.javarush.jira.profile.internal.model.Profile;
import com.javarush.jira.ref.RefType;
import com.javarush.jira.ref.RefTo;
import com.javarush.jira.ref.ReferenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProfileRestControllerIT {

    private static final long TEST_USER_ID = 2L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProfileRepository profileRepository;

    @MockBean
    private ProfileMapper profileMapper;

    @MockBean
    private MessageSource messageSource;

    @BeforeEach
    void setupSecurityContext() throws Exception {
        User user = new User(TEST_USER_ID, "admin@gmail.com", "{noop}password", "Admin", "Admin", "Admin", Role.ADMIN);
        AuthUser authUser = new AuthUser(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        when(messageSource.getMessage(anyString(), any(), anyString(), any())).thenReturn("Mocked validation message");
        when(messageSource.getMessage(any(), any())).thenReturn("Mocked validation message");

        Field field = ReferenceService.class.getDeclaredField("refSelect");
        field.setAccessible(true);
        Map<String, RefTo> contactMap = Map.of(
                "telegram", new RefTo(1L, RefType.CONTACT, "telegram", "Telegram", null)
        );
        Map<RefType, Map<String, RefTo>> allRefs = Map.of(RefType.CONTACT, contactMap);
        field.set(null, allRefs);
    }

    private ContactTo createValidContact() {
        ContactTo contact = new ContactTo();
        contact.setId(TEST_USER_ID);
        contact.setCode("telegram");
        contact.setValue("tg://user123");
        return contact;
    }

    private ProfileTo createValidProfileTo() {
        ProfileTo to = new ProfileTo(TEST_USER_ID, Set.of("ACTIVITY"), Set.of(createValidContact()));
        to.setId(TEST_USER_ID);
        return to;
    }

    @Test
    @DisplayName("GET /api/profile - success")
    void getProfile_success() throws Exception {
        Profile mockProfile = new Profile();
        ProfileTo profileTo = createValidProfileTo();

        when(profileRepository.getOrCreate(anyLong())).thenReturn(mockProfile);
        when(profileMapper.toTo(mockProfile)).thenReturn(profileTo);

        mockMvc.perform(get("/api/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mailNotifications").value(hasSize(1)))
                .andExpect(jsonPath("$.contacts").value(hasSize(1)));
    }

    @Test
    @DisplayName("GET /api/profile - unauthorized")
    void getProfile_unauthorized() throws Exception {
        SecurityContextHolder.clearContext();
        mockMvc.perform(get("/api/profile"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("PUT /api/profile - success")
    void updateProfile_success() throws Exception {
        ProfileTo updated = createValidProfileTo();
        Profile existing = new Profile();
        Profile mapped = new Profile();

        when(profileRepository.getOrCreate(TEST_USER_ID)).thenReturn(existing);
        when(profileMapper.updateFromTo(any(Profile.class), any(ProfileTo.class))).thenReturn(mapped);

        mockMvc.perform(put("/api/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("PUT /api/profile - invalid: mailNotifications has blank value")
    void updateProfile_invalid_blankMailNotification() throws Exception {
        ContactTo contact = createValidContact();
        ProfileTo invalid = new ProfileTo(TEST_USER_ID, Set.of(""), Set.of(contact));
        invalid.setId(TEST_USER_ID);

        mockMvc.perform(put("/api/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("PUT /api/profile - invalid: contact value is blank")
    void updateProfile_invalid_blankContactValue() throws Exception {
        ContactTo contact = createValidContact();
        contact.setValue("");
        ProfileTo invalid = new ProfileTo(TEST_USER_ID, Set.of("ACTIVITY"), Set.of(contact));
        invalid.setId(TEST_USER_ID);

        mockMvc.perform(put("/api/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isUnprocessableEntity());
    }
}
