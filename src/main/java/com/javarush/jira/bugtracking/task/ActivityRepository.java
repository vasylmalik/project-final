package com.javarush.jira.bugtracking.task;

import com.javarush.jira.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface ActivityRepository extends BaseRepository<Activity> {
    @Query("SELECT a FROM Activity a JOIN FETCH a.author WHERE a.taskId =:taskId ORDER BY a.updated DESC")
    List<Activity> findAllByTaskIdOrderByUpdatedDesc(long taskId);

    @Query("SELECT a FROM Activity a JOIN FETCH a.author WHERE a.taskId =:taskId AND a.comment IS NOT NULL ORDER BY a.updated DESC")
    List<Activity> findAllComments(long taskId);

    @Query("SELECT a FROM Activity a JOIN FETCH a.author WHERE a.taskId =:taskId AND a.statusCode=:statusCode ORDER BY a.updated ASC")
    List<Activity> findByTaskIdAndStatusCodeOrderByUpdatedAsc(long taskId, String statusCode);

    @Query("SELECT a FROM Activity a JOIN FETCH a.author WHERE a.taskId =:taskId AND a.statusCode=:statusCode AND a.updated > :updatedAfter  ORDER BY a.updated ASC")
    List<Activity> findByTaskIdAndStatusCodeAndUpdatedAfterOrderByUpdatedAsc(long taskId, String statusCode, LocalDateTime updatedAfter);
}
