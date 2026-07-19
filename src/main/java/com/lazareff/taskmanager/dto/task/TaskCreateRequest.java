package com.lazareff.taskmanager.dto.task;

import com.lazareff.taskmanager.enums.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCreateRequest {

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title cannot be longer than 255 characters")
    private String title;

    @Size(max = 5000, message = "Description cannot be longer than 5000 characters")
    private String description;

    @NotNull(message = "Priority is required")
    private TaskPriority priority;

}
