package com.javarush.jira.bugtracking.task;

import com.javarush.jira.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

@Transactional(readOnly = true)
public interface ActivityRepository extends BaseRepository<Activity> {
    @Query("SELECT a FROM Activity a JOIN FETCH a.author WHERE a.taskId =:taskId ORDER BY a.updated DESC")
    List<Activity> findAllByTaskIdOrderByUpdatedDesc(long taskId);

    @Query("SELECT a FROM Activity a JOIN FETCH a.author WHERE a.taskId =:taskId AND a.comment IS NOT NULL ORDER BY a.updated DESC")
    List<Activity> findAllComments(long taskId);

    @Modifying
    @Query(value = "INSERT INTO activity (author_id, task_id, updated, comment, status_code, priority_code, type_code, title, description, estimate) " +
            "VALUES (:authorId, :taskId, CURRENT_TIMESTAMP, :comment, :statusCode, :priorityCode, :typeCode, :title, :description, :estimate)",
            nativeQuery = true)
    void saveActivityDirectly(@Param("authorId") long authorId, @Param("taskId") long taskId,
                              @Param("comment") String comment, @Param("statusCode") String statusCode,
                              @Param("priorityCode") String priorityCode, @Param("typeCode") String typeCode,
                              @Param("title") String title, @Param("description") String description,
                              @Param("estimate") Integer estimate);
}
