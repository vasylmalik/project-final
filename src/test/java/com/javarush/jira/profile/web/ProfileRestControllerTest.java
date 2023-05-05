package com.javarush.jira.profile.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.javarush.jira.login.internal.web.UserTestData.ADMIN_MAIL;
import static com.javarush.jira.profile.web.ProfileRestController.REST_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProfileRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    String json = """
                     {
                         "id":2,
                         "lastLogin":null,
                         "mailNotifications":["two_days_before_deadline",
                                             "one_day_before_deadline",
                                             "three_days_before_deadline"
                                             ],
                         "contacts":[{"code":"github",
                                     "value":"adminGitHub"},
                                     {"code":"tg","value":"adminTg"}
                                     ]
                     }
                     """;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
