package com.javarush.jira.bugtracking.internal.mapper;

import com.javarush.jira.bugtracking.internal.model.Task;
import com.javarush.jira.bugtracking.to.TaskTo;
import com.javarush.jira.common.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", uses = {SprintMapper.class, ProjectMapper.class})
public interface TaskMapper extends BaseMapper<Task, TaskTo> {

    @Mapping(target = "enabled", expression = "java(task.isEnabled())")
    @Mapping(target = "activities", ignore = true)
    @Override
    TaskTo toTo(Task task);

    @Override
    Task toEntity(TaskTo taskTo);

    @Override
    List<TaskTo> toToList(Collection<Task> tasks);
}
