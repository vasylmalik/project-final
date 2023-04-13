package com.javarush.jira.bugtracking.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.javarush.jira.common.to.BaseTo;
import com.javarush.jira.common.util.validation.NoHtml;
import com.javarush.jira.login.User;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@EqualsAndHashCode(callSuper = true)
public class ActivityTo extends BaseTo {
    @NotNull
    User author;

    @NotNull
    TaskTo task;

    @Nullable
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    LocalDateTime updated;

    @NoHtml
    @Nullable
    @Size(max = 4096)
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

    @Nullable
    @Positive
    Integer estimate;

    public ActivityTo(Long id, User author, TaskTo task, LocalDateTime updated, String comment, String statusCode, String priorityCode, String typeCode, Integer estimate) {
        super(id);
        this.author = author;
        this.task = task;
        this.updated = updated;
        this.comment = comment;
        this.statusCode = statusCode;
        this.priorityCode = priorityCode;
        this.typeCode = typeCode;
        this.estimate = estimate;
    }
}
