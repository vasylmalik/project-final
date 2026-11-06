package com.javarush.jira.bugtracking.task.mapper;

import com.javarush.jira.bugtracking.task.Task;
import com.javarush.jira.bugtracking.task.to.TaskTo;
import com.javarush.jira.common.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper extends BaseMapper<Task, TaskTo> {

    @Override
    TaskTo toTo(Task task);
}
