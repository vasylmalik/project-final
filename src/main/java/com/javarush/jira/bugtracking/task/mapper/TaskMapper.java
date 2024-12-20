package com.javarush.jira.bugtracking.task.mapper;

import com.javarush.jira.bugtracking.task.Task;
import com.javarush.jira.bugtracking.task.to.TaskDTO;
import com.javarush.jira.common.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper extends BaseMapper<Task, TaskDTO> {

    @Override
    TaskDTO toDTO(Task task);
}
