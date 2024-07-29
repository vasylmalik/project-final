package com.javarush.jira.profile.internal.web;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.common.util.JsonUtil;
import com.javarush.jira.profile.ProfileTo;
import com.javarush.jira.profile.internal.ProfileMapper;
import com.javarush.jira.profile.internal.ProfileRepository;
import com.javarush.jira.profile.internal.model.Profile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.javarush.jira.login.internal.web.UserTestData.*;
import static com.javarush.jira.profile.internal.web.ProfileTestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProfileRestControllerTest extends AbstractControllerTest {
@Autowired
private ProfileRepository repository;
@Autowired
ProfileMapper mapper;

@Test
void get_WhenUnauthorized_ReturnUnauthorized() throws Exception {
    perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL))
            .andExpect(status().isUnauthorized());
}

@Test
@WithUserDetails(value = USER_MAIL)
public void get_WhenAuthorizedAsUser_ReturnUserProfile() throws Exception {
    perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(PROFILE_TO_MATCHER.contentJson(USER_PROFILE_TO));
}

@Test
@WithUserDetails(value = ADMIN_MAIL)
public void get_WhenAuthorizedAsAdmin_ReturnAdminProfile() throws Exception {
    perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(PROFILE_TO_MATCHER.contentJson(ADMIN_PROFILE_TO));
}

@Test
@WithUserDetails(value = GUEST_MAIL)
public void get_WhenAuthorizedAsGuest_ReturnEmptyProfile() throws Exception {
    perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(PROFILE_TO_MATCHER.contentJson(GUEST_PROFILE_EMPTY_TO));
}

@Test
void update_WhenUnauthorized_ReturnUnauthorized() throws Exception {
    perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
}

@Test
@WithUserDetails(value = USER_MAIL)
public void update_WhenExistingValidProfile_SaveChangesToDb() throws Exception {
    ProfileTo updatedProfileTo = getUpdatedTo(USER_ID);

    perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.writeValue(updatedProfileTo)))
            .andDo(print())
            .andExpect(status().isNoContent());

    // load updated profile from db
    Profile afterUpdateProfile = repository.getExisted(USER_ID);
    ProfileTo afterUpdateProfileTo = mapper.toTo(afterUpdateProfile);

    assertAll(
            () -> assertEquals(afterUpdateProfileTo.id(), updatedProfileTo.id()),
            () -> assertThat(afterUpdateProfileTo.getContacts()).hasSameElementsAs(updatedProfileTo.getContacts()),
            () -> assertThat(afterUpdateProfileTo.getMailNotifications()).hasSameElementsAs(updatedProfileTo.getMailNotifications())
    );
}

@Test
@WithUserDetails(value = USER_MAIL)
public void update_WhenInconsistentId_ReturnUnprocessable() throws Exception {
    ProfileTo updatedProfileTo = getUpdatedTo(ADMIN_ID);

    perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.writeValue(updatedProfileTo)))
            .andDo(print())
            .andExpect(status().isUnprocessableEntity());
}

@Test
@WithUserDetails(value = USER_MAIL)
public void update_WhenNullId__SaveChangesToDbForAuthorizedUserProfile() throws Exception {
    ProfileTo updatedProfileTo = getUpdatedTo(null);

    perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.writeValue(updatedProfileTo)))
            .andDo(print())
            .andExpect(status().isNoContent());

    // load updated profile from db
    Profile afterUpdateProfile = repository.getExisted(USER_ID);
    ProfileTo afterUpdateProfileTo = mapper.toTo(afterUpdateProfile);
    updatedProfileTo.setId(USER_ID);

    assertAll(
            () -> assertEquals(afterUpdateProfileTo.id(), updatedProfileTo.id()),
            () -> assertThat(afterUpdateProfileTo.getContacts()).hasSameElementsAs(updatedProfileTo.getContacts()),
            () -> assertThat(afterUpdateProfileTo.getMailNotifications()).hasSameElementsAs(updatedProfileTo.getMailNotifications())
    );
}

@Test
@WithUserDetails(value = USER_MAIL)
public void update_WhenUnknownContact_ReturnUnprocessable() throws Exception {
    ProfileTo updatedProfileTo = getWithUnknownContactTo(USER_ID);

    perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.writeValue(updatedProfileTo)))
            .andDo(print())
            .andExpect(status().isUnprocessableEntity());
}

@Test
@WithUserDetails(value = USER_MAIL)
public void update_WhenInvalidProfile_ReturnUnprocessable() throws Exception {
    ProfileTo updatedProfileTo = getInvalidTo(USER_ID);

    perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.writeValue(updatedProfileTo)))
            .andDo(print())
            .andExpect(status().isUnprocessableEntity());
}

@Test
@WithUserDetails(value = USER_MAIL)
public void update_WhenUnknownNotifications_ReturnUnprocessable() throws Exception {
    ProfileTo updatedProfileTo = getWithUnknownNotificationTo(USER_ID);

    perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.writeValue(updatedProfileTo)))
            .andDo(print())
            .andExpect(status().isUnprocessableEntity());
}

@Test
@WithUserDetails(value = USER_MAIL)
public void update_WhenContactHtmlUnsafe_ReturnUnprocessable() throws Exception {
    ProfileTo updatedProfileTo = getWithContactHtmlUnsafeTo(USER_ID);

    perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.writeValue(updatedProfileTo)))
            .andDo(print())
            .andExpect(status().isUnprocessableEntity());
}
}