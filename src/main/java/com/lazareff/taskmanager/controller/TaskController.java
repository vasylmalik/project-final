package com.lazareff.taskmanager.controller;

import com.lazareff.taskmanager.dto.task.TaskUpdateRequest;
import com.lazareff.taskmanager.enums.TaskStatus;
import com.lazareff.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.lazareff.taskmanager.dto.task.TaskCreateRequest;
import com.lazareff.taskmanager.dto.task.TaskResponse;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public TaskResponse create(
            @Valid @RequestBody TaskCreateRequest request) {

        return taskService.create(request);

    }

    @GetMapping("/{id}")
    public TaskResponse getById(
            @PathVariable Long id) {

        return taskService.getById(id);

    }

    @GetMapping
    public List<TaskResponse> getAll(
            @RequestParam(required = false) TaskStatus status) {

        return taskService.getAll(status);
    }

    @PutMapping("/{id}")
    public TaskResponse update(
            @PathVariable Long id,
            @Valid @RequestBody TaskUpdateRequest request) {

        return taskService.update(id, request);

    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id) {

        taskService.delete(id);

    }

}
