package com.javarush.jira.bugtracking.task.to;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
public class TaskToTags extends TaskToExt {
    Set<String> tags;

    public TaskToTags(Long id, String code, String title, String description,
                      String typeCode, String statusCode, String priorityCode,
                      LocalDateTime updated, Integer estimate, Long parentId,
                      long projectId, Long sprintId, Set<String> tags) {
        super(id, code, title, description, typeCode, statusCode, priorityCode, updated, estimate, parentId, projectId, sprintId);

        this.tags = (tags == null) ? Set.of() : Set.copyOf(tags);
    }
}
