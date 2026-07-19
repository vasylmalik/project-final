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
import com.lazareff.taskmanager.service.CommentService;
import com.lazareff.taskmanager.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final CommentMapper commentMapper;
    private final CurrentUserService currentUserService;

    @Override
    public CommentResponse create(Long taskId, CommentCreateRequest request) {

        User currentUser = currentUserService.getCurrentUser();

        log.info("Creating comment for task: {}, User: {}",
                taskId,
                currentUser.getEmail());

        Task task = taskRepository
                .findByIdAndUser(taskId, currentUser)
                .orElseThrow(() -> {
                    log.warn("Comment creation failed. Task not found. Id: {}, User: {}",
                            taskId,
                            currentUser.getEmail());

                    return new TaskNotFoundException(
                            "Task with id " + taskId + " not found"
                    );
                });

        Comment comment = commentMapper.toEntity(request);

        comment.setTask(task);
        comment.setUser(currentUser);

        comment = commentRepository.save(comment);

        log.info("Comment created successfully. Id: {}, Task: {}, User: {}",
                comment.getId(),
                taskId,
                currentUser.getEmail());

        return commentMapper.toResponse(comment);
    }

    @Override
    public List<CommentResponse> getAllByTask(Long taskId) {

        User currentUser = currentUserService.getCurrentUser();

        taskRepository.findByIdAndUser(taskId, currentUser)
                .orElseThrow(() -> {
                    log.warn("Comments retrieval failed. Task not found. Id: {}, User: {}",
                            taskId,
                            currentUser.getEmail());

                    return new TaskNotFoundException(
                            "Task with id " + taskId + " not found"
                    );
                });

        return commentRepository.findAllByTaskId(taskId)
                .stream()
                .map(commentMapper::toResponse)
                .toList();
    }

    @Override
    public CommentResponse update(Long commentId, CommentUpdateRequest request) {

        User currentUser = currentUserService.getCurrentUser();

        Comment comment = commentRepository
                .findByIdAndUser(commentId, currentUser)
                .orElseThrow(() -> {
                    log.warn("Comment update failed. Comment not found. Id: {}, User: {}",
                            commentId,
                            currentUser.getEmail());

                    return new CommentNotFoundException(
                            "Comment with id " + commentId + " not found"
                    );
                });

        commentMapper.update(request, comment);

        comment = commentRepository.save(comment);

        log.info("Comment updated successfully. Id: {}, User: {}",
                comment.getId(),
                currentUser.getEmail());

        return commentMapper.toResponse(comment);
    }

    @Override
    public void delete(Long commentId) {

        User currentUser = currentUserService.getCurrentUser();

        Comment comment = commentRepository
                .findByIdAndUser(commentId, currentUser)
                .orElseThrow(() -> {
                    log.warn("Comment deletion failed. Comment not found. Id: {}, User: {}",
                            commentId,
                            currentUser.getEmail());

                    return new CommentNotFoundException(
                            "Comment with id " + commentId + " not found"
                    );
                });

        commentRepository.delete(comment);

        log.info("Comment deleted successfully. Id: {}, User: {}",
                commentId,
                currentUser.getEmail());
    }
}
