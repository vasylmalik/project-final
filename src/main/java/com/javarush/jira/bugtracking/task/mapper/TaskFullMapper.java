package com.javarush.jira.bugtracking.task.mapper;

import com.javarush.jira.bugtracking.task.Task;
import com.javarush.jira.bugtracking.task.to.TaskToFull;
import com.javarush.jira.common.BaseMapper;
import com.javarush.jira.common.TimestampMapper;
import org.mapstruct.Mapper;

@Mapper(config = TimestampMapper.class)
public interface TaskFullMapper extends BaseMapper<Task, TaskToFull> {

    @Override
    TaskToFull toTo(Task task);
}
