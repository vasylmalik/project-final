package com.javarush.jira.bugtracking.internal.repository;

import com.javarush.jira.bugtracking.attachment.Attachment;
import com.javarush.jira.bugtracking.internal.model.UserBelong;
import com.javarush.jira.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface UserBelongRepository extends BaseRepository<UserBelong> {

    @Query("SELECT u FROM UserBelong u WHERE u.userId =:userId")
    List<UserBelong> getByUserId(Long userId);

}
