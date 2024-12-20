package com.javarush.jira.bugtracking.task.to;

import com.javarush.jira.common.HasCode;
import com.javarush.jira.common.HasIdAndParentId;
import com.javarush.jira.common.to.TitleDTO;
import com.javarush.jira.common.util.validation.Code;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
public class TaskDTO extends TitleDTO implements HasCode, HasIdAndParentId {
    @Code
    private final String typeCode;

    Long parentId;

    @NotNull
    Long projectId;

    Long sprintId;

    @Setter
    @Code
    private String statusCode;

    public TaskDTO(Long id, String code, String title, String typeCode, String statusCode, Long parentId, Long projectId, Long sprintId) {
        super(id, code, title);
        this.typeCode = typeCode;
        this.statusCode = statusCode;
        this.parentId = parentId;
        this.projectId = projectId;
        this.sprintId = sprintId;
    }
}
