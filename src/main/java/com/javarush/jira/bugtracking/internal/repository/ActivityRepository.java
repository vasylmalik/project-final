package com.javarush.jira.bugtracking.internal.repository;

import com.javarush.jira.bugtracking.internal.model.Activity;
import com.javarush.jira.bugtracking.internal.model.Task;
import com.javarush.jira.common.BaseRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface ActivityRepository extends BaseRepository<Activity> { // TODO: 8.add task summary
    List<Activity> findByTaskAndUpdatedNotNullAndStatusCodeNotNull(@NotNull Task task);
}
