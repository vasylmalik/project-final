package com.javarush.jira.bugtracking.sprint.to;

import com.javarush.jira.common.to.CodeDTO;
import lombok.Value;

@Value
public class SprintDTOFull extends SprintDTO {
    CodeDTO project;

    public SprintDTOFull(Long id, String code, String statusCode, CodeDTO project) {
        super(id, code, statusCode, project.getId());
        this.project = project;
    }
}
