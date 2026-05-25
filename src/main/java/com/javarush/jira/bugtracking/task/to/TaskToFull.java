package com.javarush.jira.bugtracking.task.to;

import com.javarush.jira.common.to.CodeTo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class TaskToFull extends TaskToExt {
    CodeTo parent;
    CodeTo project;
    CodeTo sprint;
    @Setter
    List<ActivityTo> activityTos;

    public TaskToFull(Long id, String code, String title, String description, String typeCode, String statusCode, String priorityCode,
                      LocalDateTime updated, Integer estimate, CodeTo parent, CodeTo project, CodeTo sprint, List<ActivityTo> activityTos) {
        super(id, code, title, description, typeCode, statusCode, priorityCode, updated, estimate,
                parent == null ? null : parent.getId(), project.getId(), sprint == null ? null : sprint.getId());
        this.parent = parent;
        this.project = project;
        this.sprint = sprint;
        this.activityTos = activityTos;
    }
}
