package com.javarush.jira.bugtracking.internal.repository;

import com.javarush.jira.bugtracking.internal.model.Task;
import com.javarush.jira.common.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface TaskRepository extends BaseRepository<Task> {
    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.project JOIN FETCH t.sprint LEFT JOIN FETCH t.activities")
    List<Task> getAll();

    @Query(value = "SELECT t FROM Task t LEFT JOIN FETCH t.project LEFT JOIN FETCH t.sprint LEFT JOIN FETCH t.activities WHERE t.sprint is null", // TODO: 12.add backlog
            countQuery = "SELECT count(t) FROM Task t WHERE t.sprint is null")
    Page<Task> findBySprintIsNull(Pageable pageable);
}