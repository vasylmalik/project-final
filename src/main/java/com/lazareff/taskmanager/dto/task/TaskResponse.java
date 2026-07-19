package com.lazareff.taskmanager.dto.task;

import com.lazareff.taskmanager.enums.TaskPriority;
import com.lazareff.taskmanager.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskResponse {

    private Long id;

    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private Long userId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
