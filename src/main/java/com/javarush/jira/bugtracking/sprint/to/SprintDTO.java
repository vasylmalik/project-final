package com.javarush.jira.bugtracking.sprint.to;

import com.javarush.jira.common.HasCode;
import com.javarush.jira.common.to.CodeDTO;
import com.javarush.jira.common.util.validation.Code;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SprintDTO extends CodeDTO implements HasCode {
    @Code
    String statusCode;
    @NotNull
    Long projectId;

    public SprintDTO(Long id, String code, String statusCode, Long projectId) {
        super(id, code);
        this.statusCode = statusCode;
        this.projectId = projectId;
    }
}
