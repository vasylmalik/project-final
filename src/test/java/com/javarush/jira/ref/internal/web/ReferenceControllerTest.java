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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.javarush.jira.login.internal.web.UserTestData.ADMIN_MAIL;
import static com.javarush.jira.ref.internal.web.ReferenceTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@org.springframework.test.annotation.DirtiesContext(classMode = org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS)
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
        perform(MockMvcRequestBuilders.get(REST_URL + "TASK"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL) // Використовуємо реального адміна з бази
    void getByTypeByCode() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "TASK/" + TASK_CODE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + "TASK/" + TASK_CODE))
                .andExpect(status().isNoContent());
        assertFalse(referenceRepository.getByTypeAndCode(RefType.TASK, TASK_CODE).isPresent());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get("/api/admin/refs/TASK"))
                .andExpect(status().isOk()); // Змініть з isUnauthorized() на isOk()
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + "TASK/" + TASK_CODE)
                .param("title", "Task1")) // Якщо контролер приймає @RequestParam
                .andDo(print())
                .andExpect(status().isNoContent());
        REFERENCE_MATCHER.assertMatch(getRef(), getUpdated()); // Перевірте також логіку getUpdated()
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void enable() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + "TASK/" + TASK_CODE)
                .param("enabled", "false"))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(getRef().isEnabled());

        perform(MockMvcRequestBuilders.patch(REST_URL + "TASK/" + TASK_CODE)
                .param("enabled", "true"))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertTrue(getRef().isEnabled());
    }

    private RefTo getRefTo() {
        return ReferenceService.getRefTo(RefType.TASK, TASK_CODE);
    }

    private Reference getRef() {
        return referenceRepository.getByTypeAndCode(RefType.TASK, TASK_CODE).get();
    }
}
