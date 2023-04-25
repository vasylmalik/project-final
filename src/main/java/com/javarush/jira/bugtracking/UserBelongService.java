package com.javarush.jira.bugtracking;

import com.javarush.jira.bugtracking.internal.model.UserBelong;
import com.javarush.jira.bugtracking.internal.repository.UserBelongRepository;
import com.javarush.jira.login.Role;
import org.springframework.stereotype.Service;

@Service
public class UserBelongService {
    private final UserBelongRepository repository;

    public UserBelongService(UserBelongRepository repository) {
        this.repository = repository;
    }

    public void subscribeToTask(long userId, long taskId) {
        UserBelong userBelong = repository.getByObjectId(taskId);

        if (userBelong.getUserId() != userId) {
            UserBelong subscriber = new UserBelong(taskId,
                    userBelong.getObjectType(),
                    userId,
                    Role.SUBSCRIBER.name());

            repository.saveAndFlush(subscriber);
        }
        else {
            System.exit(1);
        }
    }
}
