package com.javarush.jira.bugtracking;

import com.javarush.jira.bugtracking.internal.model.UserBelong;
import com.javarush.jira.bugtracking.internal.repository.UserBelongRepository;
import com.javarush.jira.bugtracking.to.BelongType;
import com.javarush.jira.bugtracking.to.ObjectType;
import com.javarush.jira.login.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserBelongService {

    @Autowired
    private UserBelongRepository repository;

    public void subscribeToTask(User user, long taskId, String userTypeCode) {
        List<UserBelong> userBelongAssignToTaskList = repository.findUserBelongByUserIdAndByObjectTypeAndObjectId(user.getId(), ObjectType.TASK, taskId);
        boolean alreadyIsViewer = userBelongAssignToTaskList.stream().anyMatch(userBelong -> userBelong.getBelongType().equals(BelongType.SUBSCRIBE));
        if (alreadyIsViewer) {
            return;
        }
        UserBelong userBelong = prepareUserBelong(taskId, user.getId(), userTypeCode);
        repository.save(userBelong);
    }

    private UserBelong prepareUserBelong(Long taskId, Long userId, String userTypeCode) {
        UserBelong userBelong = new UserBelong();
        userBelong.setObjectId(taskId);
        userBelong.setObjectType(ObjectType.TASK);
        userBelong.setUserId(userId);
        userBelong.setUserTypeCode(userTypeCode);
        userBelong.setBelongType(BelongType.SUBSCRIBE);
        return userBelong;
    }

}
