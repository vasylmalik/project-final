package com.lazareff.taskmanager.service;

import com.lazareff.taskmanager.dto.comment.CommentCreateRequest;
import com.lazareff.taskmanager.dto.comment.CommentResponse;
import com.lazareff.taskmanager.dto.comment.CommentUpdateRequest;

import java.util.List;

public interface CommentService {

    CommentResponse create(Long taskId, CommentCreateRequest request);

    List<CommentResponse> getAllByTask(Long taskId);

    CommentResponse update(Long commentId, CommentUpdateRequest request);

    void delete(Long commentId);

}
