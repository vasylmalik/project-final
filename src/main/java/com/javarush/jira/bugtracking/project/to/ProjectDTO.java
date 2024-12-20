package com.javarush.jira.bugtracking.project.to;

import com.javarush.jira.common.to.TitleDTO;
import com.javarush.jira.common.util.validation.Code;
import com.javarush.jira.common.util.validation.Description;
import lombok.Getter;

@Getter
public class ProjectDTO extends TitleDTO {
    @Description
    String description;

    @Code
    String typeCode;

    Long parentId;

    public ProjectDTO(Long id, String code, String title, String description, String typeCode, Long parentId) {
        super(id, code, title);
        this.description = description;
        this.typeCode = typeCode;
        this.parentId = parentId;
    }
}
