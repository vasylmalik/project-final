package com.javarush.jira.ref.internal.web;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.ref.RefTo;
import com.javarush.jira.ref.RefType;
import com.javarush.jira.ref.ReferenceService;
import com.javarush.jira.ref.internal.Reference;
import com.javarush.jira.ref.internal.ReferenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.javarush.jira.common.util.JsonUtil.writeValue;
import static com.javarush.jira.login.internal.web.UserTestData.ADMIN_MAIL;
import static com.javarush.jira.ref.internal.web.ReferenceTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ReferenceControllerTest extends AbstractControllerTest {
    private static final String REST_URL = ReferenceController.REST_URL + "/";

    @Autowired
    private ReferenceRepository referenceRepository;
    @Autowired
    private ReferenceService referenceService;

    @BeforeEach
    void reInit() {
        referenceService.updateRefs(RefType.TASK);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getByType() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RefType.TASK))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getByTypeByCode() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RefType.TASK + "/" + TASK_CODE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(REFTO_MATCHER.contentJson(refTo));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RefType.TASK + "/" + TASK_CODE))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(IllegalArgumentException.class, this::getRefTo);
        assertFalse(referenceRepository.getByTypeAndCode(RefType.TASK, TASK_CODE).isPresent());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RefType.TASK))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + RefType.TASK + "/" + TASK_CODE)
                .param("title", "Task1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
        REFERENCE_MATCHER.assertMatch(getRef(), getUpdated());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void create() throws Exception {
        Reference newRef = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(ReferenceController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(new RefTo(null, RefType.TASK, "enhancement", "Enhancement", null))))
                .andExpect(status().isCreated());
        Reference created = REFERENCE_MATCHER.readFromJson(action);
        REFERENCE_MATCHER.assertMatch(created, newRef);
        REFERENCE_MATCHER.assertMatch(referenceRepository.getExisted(created.id()), newRef);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createNoBody() throws Exception {
        perform(MockMvcRequestBuilders.post(ReferenceController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void enable() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + RefType.TASK + "/" + TASK_CODE)
                .param("enabled", "false")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(getRef().isEnabled());
        assertFalse(getRefTo().isEnabled());

        perform(MockMvcRequestBuilders.patch(REST_URL + RefType.TASK + "/" + TASK_CODE)
                .param("enabled", "true")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertTrue(getRef().isEnabled());
        assertTrue(getRefTo().isEnabled());
    }

    @NonNull
    private RefTo getRefTo() {
        return ReferenceService.getRefTo(RefType.TASK, TASK_CODE);
    }

    @NonNull
    private Reference getRef() {
        return referenceRepository.getByTypeAndCode(RefType.TASK, TASK_CODE).get();
    }
}
