package com.javarush.jira.bugtracking.task.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.javarush.jira.common.util.validation.Code;
import com.javarush.jira.common.util.validation.Description;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
public class TaskToExt extends TaskTo {
    @Description
    String description;

    @Code
    String priorityCode;

    @Nullable
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    LocalDateTime updated;

    @Nullable
    @Positive
    Integer estimate;

    public TaskToExt(Long id, String code, String title, String description, String typeCode, String statusCode, String priorityCode,
                     LocalDateTime updated, Integer estimate, Long parentId, long projectId, Long sprintId) {
        super(id, code, title, typeCode, statusCode, parentId, projectId, sprintId);
        this.description = description;
        this.priorityCode = priorityCode;
        this.updated = updated;
        this.estimate = estimate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskToExt taskToExt)) return false;
        return Objects.equals(id, taskToExt.id) &&
                Objects.equals(title, taskToExt.title) &&
                Objects.equals(getTypeCode(), taskToExt.getTypeCode()) &&
                Objects.equals(getStatusCode(), taskToExt.getStatusCode()) &&
                Objects.equals(priorityCode, taskToExt.priorityCode) &&
                Objects.equals(description, taskToExt.description) &&
                Objects.equals(estimate, taskToExt.estimate) &&
                Objects.equals(parentId, taskToExt.parentId) &&
                Objects.equals(projectId, taskToExt.projectId) &&
                Objects.equals(sprintId, taskToExt.sprintId);
    }
}
