package com.javarush.jira.bugtracking.task.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.javarush.jira.bugtracking.task.HasAuthorId;
import com.javarush.jira.common.to.BaseDTO;
import com.javarush.jira.common.util.validation.NoHtml;
import com.javarush.jira.login.UserDTO;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@EqualsAndHashCode(callSuper = true)
public class ActivityDTO extends BaseDTO implements HasAuthorId {
    @NotNull
    Long taskId;

    @Nullable
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    UserDTO author;

    @NotNull
    Long authorId;

    @Nullable
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    LocalDateTime updated;

    @NoHtml
    @Size(max = 4096)
    @Nullable
    String comment;

    @NoHtml
    @Size(min = 2, max = 32)
    @Nullable
    String statusCode;

    @NoHtml
    @Size(min = 2, max = 32)
    @Nullable
    String priorityCode;

    @NoHtml
    @Size(min = 2, max = 32)
    @Nullable
    String typeCode;

    @NoHtml
    @Size(max = 1024)
    @Nullable
    String title;

    @NoHtml
    @Size(max = 4096)
    @Nullable
    String description;

    @Positive
    @Nullable
    Integer estimate;

    public ActivityDTO(Long id, Long taskId, Long authorId, LocalDateTime updated, String comment, String statusCode,
                       String priorityCode, String typeCode, String title, String description, Integer estimate, UserDTO author) {
        super(id);
        this.taskId = taskId;
        this.authorId = authorId;
        this.updated = updated;
        this.comment = comment;
        this.statusCode = statusCode;
        this.priorityCode = priorityCode;
        this.typeCode = typeCode;
        this.title = title;
        this.description = description;
        this.estimate = estimate;
        this.author = author;
    }
}
