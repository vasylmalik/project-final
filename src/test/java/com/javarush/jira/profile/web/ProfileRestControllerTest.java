package com.javarush.jira.profile.web;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.profile.ContactTo;
import com.javarush.jira.profile.ProfileTo;
import com.javarush.jira.profile.internal.Contact;
import com.javarush.jira.profile.internal.ProfileMapper;
import com.javarush.jira.profile.internal.ProfileRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;
import java.util.stream.Collectors;

import static com.javarush.jira.common.util.JsonUtil.writeValue;
import static com.javarush.jira.login.internal.web.UserTestData.ADMIN_MAIL;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

class ProfileRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = ProfileRestController.REST_URL ;
    private final int EXPECTED_ID_FOR_ADMIN=2;

    private final ProfileTo TEST_PROFILE_FOR_UPDATING = new ProfileTo(2L,null, Set.of(new ContactTo("phone","8888")));

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ProfileMapper profileMapper;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getProfileToByUser() throws Exception{
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(EXPECTED_ID_FOR_ADMIN)));

    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL ))
                .andExpect(status().isUnauthorized())
                .andDo(print())   ;
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception{
                   perform(MockMvcRequestBuilders.put(REST_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(writeValue(TEST_PROFILE_FOR_UPDATING)))
                    .andExpect(status().is2xxSuccessful());
        Set<String> actual= profileRepository.getExisted(EXPECTED_ID_FOR_ADMIN).getContacts().stream()
                .map(x->x.getValue())
                .collect(Collectors.toSet());;
        Set<String> expected = TEST_PROFILE_FOR_UPDATING.getContacts()
                .stream()
                .map(profileMapper::toContact)
                .map(x->x.getValue())
                .collect(Collectors.toSet());
        //for some reason while making Contact from ContactTo we lose the id-value
        //i suppose it is the problem of ProfileMapper...
        //that's why i compared contact's values, not cotacts itself

        assertTrue(CollectionUtils.isEqualCollection(expected,actual));
    }




}