package com.javarush.jira.bugtracking.project.to;

import com.javarush.jira.common.to.CodeDTO;
import lombok.Value;

@Value
public class ProjectDTOFull extends ProjectDTO {
    CodeDTO parent;

    public ProjectDTOFull(Long id, String code, String title, String description, String typeCode, CodeDTO parent) {
        super(id, code, title, description, typeCode, parent == null ? null : parent.getId());
        this.parent = parent;
    }
}