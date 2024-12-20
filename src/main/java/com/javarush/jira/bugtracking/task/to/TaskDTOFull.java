package com.javarush.jira.bugtracking.task.to;

import com.javarush.jira.common.to.CodeDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class TaskDTOFull extends TaskDTOExt {
    CodeDTO parent;
    CodeDTO project;
    CodeDTO sprint;
    @Setter
    List<ActivityDTO> activityDTOs;

    public TaskDTOFull(Long id, String code, String title, String description, String typeCode, String statusCode, String priorityCode,
                       LocalDateTime updated, Integer estimate, CodeDTO parent, CodeDTO project, CodeDTO sprint, List<ActivityDTO> activityDTOs) {
        super(id, code, title, description, typeCode, statusCode, priorityCode, updated, estimate,
                parent == null ? null : parent.getId(), project.getId(), sprint == null ? null : sprint.getId());
        this.parent = parent;
        this.project = project;
        this.sprint = sprint;
        this.activityDTOs = activityDTOs;
    }
}
