package com.javarush.jira.bugtracking.task.mapper;

import com.javarush.jira.bugtracking.project.ProjectMapper;
import com.javarush.jira.bugtracking.sprint.SprintMapper;
import com.javarush.jira.bugtracking.task.Task;
import com.javarush.jira.bugtracking.task.to.TaskDTOExt;
import com.javarush.jira.common.BaseMapper;
import com.javarush.jira.common.TimestampMapper;
import com.javarush.jira.common.error.DataConflictException;
import com.javarush.jira.login.AuthUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = TimestampMapper.class, uses = {SprintMapper.class, ProjectMapper.class})
public interface TaskExtMapper extends BaseMapper<Task, TaskDTOExt> {

    static long checkProjectBelong(long projectId, Task dbTask) {
        if (projectId != dbTask.getProjectId())
            throw new DataConflictException("Task " + dbTask.id() + " doesn't belong to Project " + projectId);
        return projectId;
    }

    static long checkUserAuthorities(long sprintId, Task dbTask) {
        if (sprintId != dbTask.getSprintId() && !(AuthUser.get().isAdmin() || AuthUser.get().isManager()))
            throw new DataConflictException("Do not have authorities to change task's sprint");
        return sprintId;
    }

    @Override
    TaskDTOExt toDTO(Task task);

    @Override
    @Mapping(target = "projectId", expression = "java(TaskExtMapper.checkProjectBelong(taskDTOExt.getProjectId(), task))")
    @Mapping(target = "sprintId", expression = "java(TaskExtMapper.checkUserAuthorities(taskDTOExt.getSprintId(), task))")
    Task updateFromDTO(TaskDTOExt taskDTOExt, @MappingTarget Task task);
}
