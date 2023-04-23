package com.javarush.jira.bugtracking;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.bugtracking.internal.model.Task;
import com.javarush.jira.bugtracking.internal.model.UserBelong;
import com.javarush.jira.bugtracking.internal.repository.TaskRepository;
import com.javarush.jira.bugtracking.internal.repository.UserBelongRepository;
import com.javarush.jira.common.util.JsonUtil;
import com.javarush.jira.login.User;
import com.javarush.jira.login.internal.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.javarush.jira.bugtracking.DashboardTestData.USER_MAIL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DashboardUIControllerTest extends AbstractControllerTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserBelongRepository userBelongRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Test
    @WithUserDetails(value = USER_MAIL)
    void addTagToTask() throws Exception { // TODO: 6. Add feature new tags

        Optional<Task> optionalTask = taskRepository.getAll().stream()
                .findAny();
        assertTrue(optionalTask.isPresent());

        Task task = optionalTask.get();
        String tag1 = "tag1";
        String tag2 = "tag2";
        Set<String> tags = task.getTags();
        while (tags.contains(tag1) || tags.contains(tag2)) {
            if (tags.contains(tag1)) {
                tag1 += "1";
            }
            if (tags.contains(tag2)) {
                tag2 += "2";
            }
        }
        List<String> newTags = List.of(tag1, tag2);

        String rest_url = "/tasks/" + task.getId() +"/tags";
        perform(MockMvcRequestBuilders.post(rest_url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTags)))
                .andExpect(status().is3xxRedirection());

        @SuppressWarnings("DataFlowIssue")
        Task updatedTask = taskRepository.getExisted(task.getId());
        assertThat(updatedTask.getTags(), Matchers.containsInAnyOrder(tag1, tag2));
    }

    @Disabled("conflict with liquibase?") // TODO: 7. Add subscribe feature
    @Transactional
    @Test
    @WithUserDetails(value = USER_MAIL)
    void addUserToTask() throws Exception {
        Optional<Task> optionalTask = taskRepository.findAll().stream()
                .findAny();
        assertTrue(optionalTask.isPresent());

        Task task = optionalTask.get();

        UserBelong userBelongTask = new UserBelong();
        userBelongTask.setObjectId(task.getId());

        List<Long> alreadyBelongedUsers = userBelongRepository.findAll().stream()
                .filter(userBelong -> userBelong.getObjectId().equals(task.getId()))
                .map(UserBelong::getUserId)
                .toList();

        List<User> all = userRepository.findAll();
        assertTrue(all.size() > 0);

        Optional<User> needToAttachUser = all.stream()
                .filter(user -> !alreadyBelongedUsers.contains(user.getId()))
                .findAny();
        assertTrue(needToAttachUser.isPresent());

        User user = needToAttachUser.get();

        perform(MockMvcRequestBuilders.post("/tasks/" + task.getId() + "/users/" + user.getId()))
                .andExpect(status().is3xxRedirection());

        userBelongTask.setUserId(user.getId());
        userBelongRepository.findOne(Example.of(userBelongTask)).ifPresentOrElse(
                userBelong -> {
                    assertThat(userBelong.getObjectId(), Matchers.equalTo(task.getId()));
                    assertThat(userBelong.getUserId(), Matchers.equalTo(user.getId()));
                },
                () -> {
                    throw new AssertionError("UserBelong not found");
                }
        );
    }
}