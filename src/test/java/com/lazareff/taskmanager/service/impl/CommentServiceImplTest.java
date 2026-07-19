package com.lazareff.taskmanager.service.impl;

import com.lazareff.taskmanager.dto.comment.CommentCreateRequest;
import com.lazareff.taskmanager.dto.comment.CommentResponse;
import com.lazareff.taskmanager.dto.comment.CommentUpdateRequest;
import com.lazareff.taskmanager.entity.Comment;
import com.lazareff.taskmanager.entity.Task;
import com.lazareff.taskmanager.entity.User;
import com.lazareff.taskmanager.exception.CommentNotFoundException;
import com.lazareff.taskmanager.exception.TaskNotFoundException;
import com.lazareff.taskmanager.mapper.CommentMapper;
import com.lazareff.taskmanager.repository.CommentRepository;
import com.lazareff.taskmanager.repository.TaskRepository;
import com.lazareff.taskmanager.service.CurrentUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void create_shouldCreateComment() {

        User user = new User();
        Task task = new Task();

        CommentCreateRequest request = new CommentCreateRequest();
        request.setContent("Test comment");

        Comment comment = new Comment();
        Comment savedComment = new Comment();
        CommentResponse response = new CommentResponse();

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(taskRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(task));
        when(commentMapper.toEntity(request)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(savedComment);
        when(commentMapper.toResponse(savedComment)).thenReturn(response);

        CommentResponse result = commentService.create(1L, request);

        assertEquals(response, result);

        verify(commentRepository).save(comment);

        assertEquals(task, comment.getTask());
        assertEquals(user, comment.getUser());
    }

    @Test
    void create_shouldThrowTaskNotFoundException() {

        User user = new User();

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(taskRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.empty());

        assertThrows(
                TaskNotFoundException.class,
                () -> commentService.create(1L, new CommentCreateRequest())
        );
    }

    @Test
    void getAllByTask_shouldReturnComments() {

        User user = new User();
        Task task = new Task();

        Comment comment = new Comment();
        CommentResponse response = new CommentResponse();

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(taskRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(task));
        when(commentRepository.findAllByTaskId(1L))
                .thenReturn(List.of(comment));
        when(commentMapper.toResponse(comment))
                .thenReturn(response);

        List<CommentResponse> result = commentService.getAllByTask(1L);

        assertEquals(1, result.size());

        verify(commentRepository).findAllByTaskId(1L);
    }

    @Test
    void getAllByTask_shouldThrowTaskNotFoundException() {

        User user = new User();

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(taskRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.empty());

        assertThrows(
                TaskNotFoundException.class,
                () -> commentService.getAllByTask(1L)
        );
    }

    @Test
    void update_shouldUpdateComment() {

        User user = new User();

        Comment comment = new Comment();

        CommentUpdateRequest request = new CommentUpdateRequest();
        request.setContent("Updated");

        CommentResponse response = new CommentResponse();

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(commentRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(comment));
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.toResponse(comment)).thenReturn(response);

        CommentResponse result = commentService.update(1L, request);

        assertEquals(response, result);

        verify(commentMapper).update(request, comment);
        verify(commentRepository).save(comment);
    }

    @Test
    void update_shouldThrowCommentNotFoundException() {

        User user = new User();

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(commentRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.empty());

        assertThrows(
                CommentNotFoundException.class,
                () -> commentService.update(1L, new CommentUpdateRequest())
        );
    }

    @Test
    void delete_shouldDeleteComment() {

        User user = new User();
        Comment comment = new Comment();

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(commentRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(comment));

        commentService.delete(1L);

        verify(commentRepository).delete(comment);
    }

    @Test
    void delete_shouldThrowCommentNotFoundException() {

        User user = new User();

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(commentRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.empty());

        assertThrows(
                CommentNotFoundException.class,
                () -> commentService.delete(1L)
        );
    }
}
