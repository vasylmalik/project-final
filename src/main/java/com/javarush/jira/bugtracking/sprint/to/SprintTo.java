package com.javarush.jira.bugtracking.sprint.to;

import com.javarush.jira.common.HasCode;
import com.javarush.jira.common.to.CodeTo;
import com.javarush.jira.common.util.validation.Code;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SprintTo extends CodeTo implements HasCode {
    @Code
    String statusCode;
    @NotNull
    Long projectId;

    public SprintTo(Long id, String code, String statusCode, Long projectId) {
        super(id, code);
        this.statusCode = statusCode;
        this.projectId = projectId;
    }
}
