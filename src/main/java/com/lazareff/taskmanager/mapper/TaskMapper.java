package com.lazareff.taskmanager.mapper;

import com.lazareff.taskmanager.dto.task.TaskCreateRequest;
import com.lazareff.taskmanager.dto.task.TaskResponse;
import com.lazareff.taskmanager.dto.task.TaskUpdateRequest;
import com.lazareff.taskmanager.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(source = "user.id", target = "userId")
    TaskResponse toResponse(Task task);

    Task toEntity(TaskCreateRequest request);

    void update(TaskUpdateRequest request, @MappingTarget Task task);

}
