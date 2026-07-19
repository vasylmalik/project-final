package com.lazareff.taskmanager.service.impl;

import com.lazareff.taskmanager.dto.task.TaskCreateRequest;
import com.lazareff.taskmanager.dto.task.TaskResponse;
import com.lazareff.taskmanager.dto.task.TaskUpdateRequest;
import com.lazareff.taskmanager.entity.Task;
import com.lazareff.taskmanager.entity.User;
import com.lazareff.taskmanager.enums.TaskStatus;
import com.lazareff.taskmanager.exception.TaskNotFoundException;
import com.lazareff.taskmanager.mapper.TaskMapper;
import com.lazareff.taskmanager.repository.TaskRepository;
import com.lazareff.taskmanager.service.CurrentUserService;
import com.lazareff.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final CurrentUserService currentUserService;

    @Override
    public TaskResponse create(TaskCreateRequest request) {

        User currentUser = currentUserService.getCurrentUser();

        log.info("Creating new task for user: {}",
                currentUser.getEmail());

        Task task = taskMapper.toEntity(request);

        task.setUser(currentUser);
        task.setStatus(TaskStatus.TODO);

        task = taskRepository.save(task);

        log.info("Task created successfully. Id: {}, Title: {}, User: {}",
                task.getId(),
                task.getTitle(),
                currentUser.getEmail());

        return taskMapper.toResponse(task);

    }

    @Override
    public TaskResponse getById(Long id) {

        User currentUser = currentUserService.getCurrentUser();

        Task task = taskRepository
                .findByIdAndUser(id, currentUser)
                .orElseThrow(() -> {
                    log.warn("Task not found. Id: {}, User: {}",
                            id,
                            currentUser.getEmail());

                    return new TaskNotFoundException(
                            "Task with id " + id + " not found"
                    );
                });

        return taskMapper.toResponse(task);

    }

    @Override
    public List<TaskResponse> getAll(TaskStatus status) {

        User currentUser = currentUserService.getCurrentUser();

        List<Task> tasks;

        if (status == null) {
            tasks = taskRepository.findAllByUser(currentUser);
        } else {
            tasks = taskRepository.findAllByUserAndStatus(currentUser, status);
        }

        return tasks.stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    @Override
    public TaskResponse update(Long id, TaskUpdateRequest request) {

        User currentUser = currentUserService.getCurrentUser();

        Task task = taskRepository
                .findByIdAndUser(id, currentUser)
                .orElseThrow(() -> {
                    log.warn("Task not found. Id: {}, User: {}",
                            id,
                            currentUser.getEmail());

                    return new TaskNotFoundException(
                            "Task with id " + id + " not found"
                    );
                });

        taskMapper.update(request, task);

        task = taskRepository.save(task);

        log.info("Task updated successfully. Id: {}, Title: {}, User: {}",
                task.getId(),
                task.getTitle(),
                currentUser.getEmail());

        return taskMapper.toResponse(task);

    }

    @Override
    public void delete(Long id) {

        User currentUser = currentUserService.getCurrentUser();

        Task task = taskRepository
                .findByIdAndUser(id, currentUser)
                .orElseThrow(() -> {
                    log.warn("Task deletion failed. Task not found. Id: {}, User: {}",
                            id,
                            currentUser.getEmail());

                    return new TaskNotFoundException(
                            "Task with id " + id + " not found"
                    );
                });

        taskRepository.delete(task);

        log.info("Task deleted successfully. Id: {}, User: {}",
                id,
                currentUser.getEmail());

    }

}
