package com.javarush.jira.profile.internal.web;

import com.javarush.jira.AbstractControllerTest;
import static com.javarush.jira.common.util.JsonUtil.writeValue;
import static com.javarush.jira.login.internal.web.UserTestData.ADMIN_MAIL;
import static com.javarush.jira.login.internal.web.UserTestData.GUEST_MAIL;
import static com.javarush.jira.login.internal.web.UserTestData.USER_ID;
import static com.javarush.jira.login.internal.web.UserTestData.USER_MAIL;
import com.javarush.jira.profile.internal.ProfileMapper;
import com.javarush.jira.profile.internal.ProfileRepository;
import com.javarush.jira.profile.internal.model.Profile;
import static com.javarush.jira.profile.internal.web.ProfileRestController.REST_URL;
import static com.javarush.jira.profile.internal.web.ProfileTestData.ADMIN_PROFILE_TO;
import static com.javarush.jira.profile.internal.web.ProfileTestData.GUEST_PROFILE_EMPTY_TO;
import static com.javarush.jira.profile.internal.web.ProfileTestData.PROFILE_MATCHER_TO;
import static com.javarush.jira.profile.internal.web.ProfileTestData.USER_PROFILE_TO;
import static com.javarush.jira.profile.internal.web.ProfileTestData.getInvalidTo;
import static com.javarush.jira.profile.internal.web.ProfileTestData.getUpdated;
import static com.javarush.jira.profile.internal.web.ProfileTestData.getUpdatedTo;
import static com.javarush.jira.profile.internal.web.ProfileTestData.getWithContactHtmlUnsafeTo;
import static com.javarush.jira.profile.internal.web.ProfileTestData.getWithUnknownContactTo;
import static com.javarush.jira.profile.internal.web.ProfileTestData.getWithUnknownNotificationTo;
import jakarta.persistence.EntityNotFoundException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ProfileRestControllerTest extends AbstractControllerTest {

  @Autowired
  private ProfileRepository repository;
  @Autowired
  private ProfileMapper mapper;


  @Test
  void shouldCreateMockMvc() {
    assertNotNull(mockMvc);
  }

  @Test
  @WithUserDetails(ADMIN_MAIL)
  void getAdminTest() throws Exception {
    perform(MockMvcRequestBuilders.get(REST_URL))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(PROFILE_MATCHER_TO.contentJson(ADMIN_PROFILE_TO));
  }
  @Test
  @WithUserDetails(USER_MAIL)
  void getUserTest() throws Exception {
    perform(MockMvcRequestBuilders.get(REST_URL))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(PROFILE_MATCHER_TO.contentJson(USER_PROFILE_TO));
  }
  @Test
  @WithUserDetails(value = GUEST_MAIL)
  void getGuestTest() throws Exception {
    perform(MockMvcRequestBuilders.get(REST_URL))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(PROFILE_MATCHER_TO.contentJson(GUEST_PROFILE_EMPTY_TO));
  }
  @Test
  @WithAnonymousUser
  void getAnonymousUser() throws Exception {
    perform(MockMvcRequestBuilders.get(REST_URL))
        .andExpect(status().isUnauthorized())
        .andDo(print());
  }

  @Test
  @WithAnonymousUser
  void updateByAnonymousUser() throws Exception {
    perform(MockMvcRequestBuilders.put(REST_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(writeValue(getInvalidTo())))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithUserDetails(value = USER_MAIL)
  void updateProfile() throws Exception {
    Profile profileToUpdate = getUpdated(USER_ID);
    perform(MockMvcRequestBuilders.put(REST_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(writeValue(getUpdatedTo())))
        .andDo(print())
        .andExpect(status().isNoContent());
    Profile probablyUpdatedProfile = repository.findById(USER_ID).orElseThrow(EntityNotFoundException::new);
    assertThat(mapper.toTo(probablyUpdatedProfile)).isEqualTo(mapper.toTo(profileToUpdate));
  }

  @Test
  @WithUserDetails(value = USER_MAIL)
  void updateWithInvalidProfile() throws Exception {
    perform(MockMvcRequestBuilders.put(REST_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(writeValue(getInvalidTo())))
        .andDo(print())
        .andExpect(status().isUnprocessableEntity());
  }

  @Test
  @WithUserDetails(value = USER_MAIL)
  void updateWithUnknownNotification() throws Exception {
    perform(MockMvcRequestBuilders.put(REST_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(writeValue(getWithUnknownNotificationTo())))
        .andDo(print())
        .andExpect(status().isUnprocessableEntity());
  }

  @Test
  @WithUserDetails(value = USER_MAIL)
  void updateWithUnknownContact() throws Exception {
    perform(MockMvcRequestBuilders.put(REST_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(writeValue(getWithUnknownContactTo())))
        .andDo(print())
        .andExpect(status().isUnprocessableEntity());
  }

  @Test
  @WithUserDetails(value = ADMIN_MAIL)
  void updateWithHtmlUnsafeContact() throws Exception {
    perform(MockMvcRequestBuilders.put(REST_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(writeValue(getWithContactHtmlUnsafeTo())))
        .andDo(print())
        .andExpect(status().isUnprocessableEntity());
  }



}