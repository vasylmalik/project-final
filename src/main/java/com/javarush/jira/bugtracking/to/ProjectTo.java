package com.javarush.jira.bugtracking.to;

import com.javarush.jira.common.util.validation.Code;
import com.javarush.jira.common.util.validation.NoHtml;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class ProjectTo extends NodeTo<ProjectTo> {
    @Code
    String code;

    @NotBlank
    @NoHtml
    @Size(min = 2, max = 4096)
    String description;

    @Code
    String typeCode;

    public ProjectTo(Long id, String title, boolean enabled, String code, String description, String typeCode, ProjectTo parent) {
        super(id, title, enabled, parent);
        this.code = code;
        this.description = description;
        this.typeCode = typeCode;
    }
}
