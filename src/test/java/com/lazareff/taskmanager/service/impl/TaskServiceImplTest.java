package com.lazareff.taskmanager.service.impl;

import com.lazareff.taskmanager.dto.task.TaskCreateRequest;
import com.lazareff.taskmanager.dto.task.TaskResponse;
import com.lazareff.taskmanager.entity.Task;
import com.lazareff.taskmanager.entity.User;
import com.lazareff.taskmanager.enums.TaskStatus;
import com.lazareff.taskmanager.mapper.TaskMapper;
import com.lazareff.taskmanager.repository.TaskRepository;
import com.lazareff.taskmanager.service.CurrentUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.lazareff.taskmanager.exception.TaskNotFoundException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.lazareff.taskmanager.dto.task.TaskUpdateRequest;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void create_shouldCreateTask() {

        TaskCreateRequest request = new TaskCreateRequest();
        request.setTitle("Test Task");

        User user = new User();

        Task task = new Task();

        Task savedTask = new Task();
        savedTask.setStatus(TaskStatus.TODO);

        TaskResponse response = new TaskResponse();

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(taskMapper.toEntity(request)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(savedTask);
        when(taskMapper.toResponse(savedTask)).thenReturn(response);

        TaskResponse result = taskService.create(request);

        assertEquals(response, result);

        verify(currentUserService).getCurrentUser();
        verify(taskMapper).toEntity(request);
        verify(taskRepository).save(task);
        verify(taskMapper).toResponse(savedTask);

        assertEquals(TaskStatus.TODO, task.getStatus());
        assertEquals(user, task.getUser());
    }

    @Test
    void getById_shouldReturnTask() {

        User user = new User();

        Task task = new Task();
        task.setId(1L);

        TaskResponse response = new TaskResponse();

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(taskRepository.findByIdAndUser(1L, user))
                .thenReturn(java.util.Optional.of(task));
        when(taskMapper.toResponse(task)).thenReturn(response);

        TaskResponse result = taskService.getById(1L);

        assertEquals(response, result);

        verify(taskRepository).findByIdAndUser(1L, user);
    }

    @Test
    void delete_shouldDeleteTask() {

        User user = new User();

        Task task = new Task();

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(taskRepository.findByIdAndUser(1L, user))
                .thenReturn(java.util.Optional.of(task));

        taskService.delete(1L);

        verify(taskRepository).delete(task);
    }

    @Test
    void getById_shouldThrowTaskNotFoundException() {

        User user = new User();

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(taskRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.empty());

        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.getById(1L)
        );

        verify(taskRepository).findByIdAndUser(1L, user);
    }

    @Test
    void update_shouldUpdateTask() {

        User user = new User();

        Task task = new Task();

        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setTitle("Updated Task");

        TaskResponse response = new TaskResponse();

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(taskRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponse(task)).thenReturn(response);

        TaskResponse result = taskService.update(1L, request);

        assertEquals(response, result);

        verify(taskMapper).update(request, task);
        verify(taskRepository).save(task);
        verify(taskMapper).toResponse(task);
    }

}
