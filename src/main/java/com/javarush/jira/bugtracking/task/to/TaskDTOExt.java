package com.javarush.jira.bugtracking.task.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.javarush.jira.common.util.validation.Code;
import com.javarush.jira.common.util.validation.Description;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
public class TaskDTOExt extends TaskDTO {
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

    Set<@Size(min = 2, max = 32) String> tags = Set.of();

    public TaskDTOExt(Long id, String code, String title, String description, String typeCode, String statusCode, String priorityCode,
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
        if (!(o instanceof TaskDTOExt taskDTOExt)) return false;
        return Objects.equals(id, taskDTOExt.id) &&
                Objects.equals(title, taskDTOExt.title) &&
                Objects.equals(getTypeCode(), taskDTOExt.getTypeCode()) &&
                Objects.equals(getStatusCode(), taskDTOExt.getStatusCode()) &&
                Objects.equals(priorityCode, taskDTOExt.priorityCode) &&
                Objects.equals(description, taskDTOExt.description) &&
                Objects.equals(estimate, taskDTOExt.estimate) &&
                Objects.equals(parentId, taskDTOExt.parentId) &&
                Objects.equals(projectId, taskDTOExt.projectId) &&
                Objects.equals(sprintId, taskDTOExt.sprintId);
    }
}
