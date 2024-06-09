package com.javarush.jira.profile.internal.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.login.AuthUser;
import com.javarush.jira.login.Role;
import com.javarush.jira.login.User;
import com.javarush.jira.profile.ProfileTo;
import com.javarush.jira.profile.internal.ProfileMapper;
import com.javarush.jira.profile.internal.ProfileRepository;
import com.javarush.jira.profile.internal.model.Profile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Arrays;
import java.util.List;

import static com.javarush.jira.login.Role.ADMIN;
import static com.javarush.jira.login.Role.DEV;
import static com.javarush.jira.profile.internal.web.ProfileRestController.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ProfileRestControllerTest extends AbstractControllerTest {
    private final static Long USER_ID = 1L;
    private final static Long PROFILE_ID=1L;
    @MockBean
    private ProfileRepository profileRepository;
    @MockBean
    private ProfileMapper mapper;

    @BeforeEach
    void init() {
        List<GrantedAuthority> roles = Arrays.asList(Role.ADMIN, Role.DEV);
        var user = new User();
        user.setId(USER_ID);
        user.setPassword("pass");
        user.setEmail("user@gmail.com");
        user.setRoles(List.of(ADMIN, DEV));
        AuthUser authUser = new AuthUser(user);
        var testingAuthenticationToken = new TestingAuthenticationToken(authUser, authUser.getPassword(), roles);
        var context = SecurityContextHolder.getContext();
        context.setAuthentication(testingAuthenticationToken);
        SecurityContextHolder.setContext(context);
    }
    @Test
    void getProfile() throws Exception {
        var profile = new Profile();
        profile.setId(PROFILE_ID);
        var profileTo = new ProfileTo(PROFILE_ID, null, null);
        when(mapper.toTo(profile)).thenReturn(profileTo);
        perform(get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    void givenRequestIsAnonymous() throws Exception {
        perform(get(REST_URL).with(anonymous()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateProfile() throws Exception {
        var updatedProfileTo = ProfileTestData.getUpdatedTo();
        var updatedProfile = ProfileTestData.getUpdated(PROFILE_ID);
        when(profileRepository.getOrCreate(anyLong())).thenReturn(updatedProfile);
        when(mapper.updateFromTo(any(Profile.class), any(ProfileTo.class))).thenReturn(updatedProfile);
        perform(put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updatedProfileTo)))
                .andExpect(status().isNoContent());
        verify(profileRepository).save(any(Profile.class));
    }
}


