package com.javarush.jira.bugtracking;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.bugtracking.internal.model.Task;
import com.javarush.jira.bugtracking.internal.repository.TaskRepository;
import com.javarush.jira.common.util.JsonUtil;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.javarush.jira.login.internal.web.UserTestData.USER_MAIL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DashboardUIControllerTest extends AbstractControllerTest {

    @Autowired
    private TaskRepository taskRepository;

    @Transactional
    @Test
    @WithUserDetails(value = USER_MAIL)
    public void addTagToTask() throws Exception {

        Optional<Task> optionalTask = taskRepository.getAll().stream()
                .findAny();
        assertTrue(optionalTask.isPresent());

        Task task = optionalTask.get();
        String[] tagsFrom = {"tag1", "tag2"};

        perform(MockMvcRequestBuilders.post("/tasks/{id}/tags", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(tagsFrom)))
                .andExpect(status().is3xxRedirection());

        Task updatedTask = taskRepository.getExisted(task.getId());
        assertThat(updatedTask.getTags(), Matchers.containsInAnyOrder("tag1", "tag2"));
    }
}