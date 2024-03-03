package com.javarush.jira.profile.internal.web;

import com.javarush.jira.AbstractControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.jira.profile.ContactTo;
import com.javarush.jira.profile.ProfileTo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
public class ProfileRestControllerTest extends AbstractControllerTest{

    private MockMvc mockMvc;

    @Mock
    private AbstractProfileController profileController;

    @InjectMocks
    private ProfileRestController profileRestController;

    @Test
    public void testGetProfile() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(profileRestController).build();

        when(profileController.get(anyLong())).thenReturn(new ProfileTo(
                null,
                Set.of("assigned", "overdue", "deadline"),
                Set.of(new ContactTo("skype", "userSkype"),
                        new ContactTo("mobile", "+01234567890"),
                        new ContactTo("website", "user.com"))
        ));

        MockHttpServletResponse response = mockMvc.perform(get(ProfileRestController.REST_URL)
                        .with(user("username").password("password").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        // Add assertions based on the expected behavior of the get() method
        // For example, you can verify the response content or specific fields

        verify(profileController, times(1)).get(anyLong());
    }

    @Test
    public void testUpdateProfile() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(profileRestController).build();

        // Create a ProfileTo object for testing
        ProfileTo profileTo = new ProfileTo(
                null,
                Set.of("three_days_before_deadline", "two_days_before_deadline", "one_day_before_deadline"),
                Set.of(new ContactTo("tg", "guestTg"))
        );

        doNothing().when(profileController).update(any(ProfileTo.class), anyLong());

        mockMvc.perform(put(ProfileRestController.REST_URL)
                        .with(user("username").password("password").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(profileTo)))
                .andExpect(status().isNoContent());

        // Add assertions based on the expected behavior of the update() method

        verify(profileController, times(1)).update(any(ProfileTo.class), anyLong());
    }
}
