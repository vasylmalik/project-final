package com.lazareff.taskmanager.controller;

import com.lazareff.taskmanager.dto.comment.CommentCreateRequest;
import com.lazareff.taskmanager.dto.comment.CommentResponse;
import com.lazareff.taskmanager.dto.comment.CommentUpdateRequest;
import com.lazareff.taskmanager.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/tasks/{taskId}/comments")
    public CommentResponse create(
            @PathVariable Long taskId,
            @Valid @RequestBody CommentCreateRequest request) {

        return commentService.create(taskId, request);
    }

    @GetMapping("/tasks/{taskId}/comments")
    public List<CommentResponse> getAllByTask(
            @PathVariable Long taskId) {

        return commentService.getAllByTask(taskId);
    }

    @PutMapping("/comments/{commentId}")
    public CommentResponse update(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRequest request) {

        return commentService.update(commentId, request);
    }

    @DeleteMapping("/comments/{commentId}")
    public void delete(
            @PathVariable Long commentId) {

        commentService.delete(commentId);
    }
}
