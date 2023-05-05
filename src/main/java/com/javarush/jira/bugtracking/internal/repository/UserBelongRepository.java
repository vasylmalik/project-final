package com.javarush.jira.bugtracking.internal.repository;

import com.javarush.jira.bugtracking.internal.model.UserBelong;
import com.javarush.jira.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface UserBelongRepository extends BaseRepository<UserBelong> {
    @Query("FROM UserBelong ub WHERE ub.objectId=:taskId")
    List<UserBelong> getByTaskId(long taskId);
}
