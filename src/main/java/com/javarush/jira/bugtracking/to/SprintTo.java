package com.javarush.jira.bugtracking.to;

import com.javarush.jira.common.to.TitleTo;
import com.javarush.jira.common.util.validation.Code;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@EqualsAndHashCode(callSuper = true)
public class SprintTo extends TitleTo {
    @Code
    String statusCode;

    LocalDateTime startpoint;

    LocalDateTime endpoint;

    @NotNull
    ProjectTo project;

    public SprintTo(Long id, String title, boolean enabled, String statusCode, LocalDateTime startpoint, LocalDateTime endpoint, ProjectTo project) {
        super(id, title, enabled);
        this.statusCode = statusCode;
        this.startpoint = startpoint;
        this.endpoint = endpoint;
        this.project = project;
    }
}
