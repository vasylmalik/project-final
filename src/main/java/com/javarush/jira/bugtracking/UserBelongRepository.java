package com.javarush.jira.bugtracking;

import com.javarush.jira.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface UserBelongRepository extends BaseRepository<UserBelong> {
    @Query("select count(u)>0 from UserBelong u where u.objectId=?1 and u.objectType=?2 and u.userId=?3 and u.userTypeCode=?4")
    boolean exists(long objectId, ObjectType type, long userId, String code);

    @Query("from UserBelong u where u.objectId=?1 order by u.startpoint desc")
    List<UserBelong> findAll(long objectId);

    @Query("SELECT u FROM UserBelong u JOIN Task t ON u.objectId = t.id JOIN Sprint s ON t.sprintId = s.id " +
            "WHERE u.userId = :userId AND u.objectType = :#{T(com.javarush.jira.bugtracking.ObjectType).TASK} " +
            "AND u.endpoint IS NULL AND t.sprintId = :sprintId")
    List<UserBelong> findActiveTaskAssignmentsForUserBySprint(long userId, long sprintId);

    @Query("SELECT u FROM UserBelong u WHERE u.objectId =?1 AND u.objectType=?2 and u.userId=?3 and u.userTypeCode=?4 and u.endpoint IS NULL")
    Optional<UserBelong> findActiveAssignment(long objectId, ObjectType objectType, long userId, String userTypeCode);
}
