package com.javarush.jira.profile.internal.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.login.AuthUser;
import com.javarush.jira.login.User;
import com.javarush.jira.login.internal.UserRepository;
import com.javarush.jira.profile.ContactTo;
import com.javarush.jira.profile.ProfileTo;
import com.javarush.jira.profile.internal.ProfileRepository;
import com.javarush.jira.profile.internal.model.Contact;
import com.javarush.jira.profile.internal.model.Profile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import static com.javarush.jira.profile.internal.web.ProfileRestController.REST_URL;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileRestControllerTest extends AbstractControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    // it's fake error
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private  ProfileRepository profileRepository;
    @Test
    public void getWithRealUser() throws Exception {
        User user = userRepository.findById(1L).get();
        AuthUser authUser = new AuthUser(user);
        mvc.perform(MockMvcRequestBuilders.get(REST_URL)
                        .with(user(authUser)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void updateProfileWithUnRealUser() throws Exception {
        User user = new User(101110L,"1234111115678@gmail.com","dgd111tg043","123","321","33322211");
        Profile profile = profileRepository.findById(1L).get();
        Set<ContactTo> contactTos = new HashSet<>();
        for (Contact contact : profile.getContacts()) {
            ContactTo contactTo = new ContactTo(contact.getCode(),contact.getValue());
            contactTos.add(contactTo);
        }
        ProfileTo profileTo = new ProfileTo(1L, Set.of(String.valueOf(profile.getMailNotifications())), contactTos);
        AuthUser authUser = new AuthUser(user);
        mvc.perform(MockMvcRequestBuilders.put(REST_URL).with(user(authUser)).contentType(MediaType.APPLICATION_JSON).content(
                objectMapper.writeValueAsString(profileTo)
        )).andExpect(status().isUnprocessableEntity());
    }

}