package com.javarush.jira.bugtracking.task;

import com.javarush.jira.common.BaseRepository;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface ActivityRepository extends BaseRepository<Activity> {
    @Query("SELECT a FROM Activity a JOIN FETCH a.author WHERE a.taskId =:taskId ORDER BY a.updated DESC")
    List<Activity> findAllByTaskIdOrderByUpdatedDesc(long taskId);

    @Query("SELECT a FROM Activity a JOIN FETCH a.author WHERE a.taskId =:taskId AND a.comment IS NOT NULL ORDER BY a.updated DESC")
    List<Activity> findAllComments(long taskId);

    Optional<Activity> findActivityByTaskIdAndStatusCode(long taskId, @Size(min = 2, max = 32) String statusCode);
}
