package com.javarush.jira.bugtracking.internal.repository;

import com.javarush.jira.bugtracking.internal.model.UserBelong;
import com.javarush.jira.bugtracking.to.ObjectType;
import com.javarush.jira.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface UserBelongRepository extends BaseRepository<UserBelong> {

    @Query("from UserBelong where userId = ?1 and objectType = ?2 and objectId = ?3")
    List<UserBelong> findUserBelongByUserIdAndByObjectTypeAndObjectId(Long userId, ObjectType objectType, Long objectId);

}
