package com.javarush.jira.bugtracking.project.to;

import com.javarush.jira.common.to.CodeTo;
import lombok.Value;

@Value
public class ProjectToFull extends ProjectTo {
    CodeTo parent;

    public ProjectToFull(Long id, String code, String title, String description, String typeCode, CodeTo parent) {
        super(id, code, title, description, typeCode, parent == null ? null : parent.getId());
        this.parent = parent;
    }
}