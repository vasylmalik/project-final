package com.javarush.jira.bugtracking;

import com.javarush.jira.bugtracking.internal.model.UserBelong;
import com.javarush.jira.bugtracking.internal.repository.UserBelongRepository;
import com.javarush.jira.login.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserBelongService {
    private final UserBelongRepository repository;

    public UserBelongService(UserBelongRepository repository) {
        this.repository = repository;
    }

    public void subscribeToTask(long userId, long taskId) {
        List<UserBelong> userBelongList = repository.getByTaskId(taskId);

        if (userBelongList.isEmpty()) {
            log.debug("Could`t find task with id={}", taskId);
            return;
        }

        //check if the current user has already assignment to this task.
        Optional<UserBelong> userBelongWithAssignment = userBelongList.stream().filter(e -> e.getUserId().equals(userId)).findFirst();

        if (userBelongWithAssignment.isEmpty()) {
            UserBelong subscriber = new UserBelong(taskId,
                    userBelongList.get(0).getObjectType(),
                    userId,
                    Role.SUBSCRIBER.name());

            repository.saveAndFlush(subscriber);
        } else {
            log.debug("Could`t find task without assignment with id={}", taskId);
        }
    }
}
