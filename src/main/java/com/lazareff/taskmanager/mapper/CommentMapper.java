package com.lazareff.taskmanager.mapper;

import com.lazareff.taskmanager.dto.comment.CommentCreateRequest;
import com.lazareff.taskmanager.dto.comment.CommentResponse;
import com.lazareff.taskmanager.dto.comment.CommentUpdateRequest;
import com.lazareff.taskmanager.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "task.id", target = "taskId")
    CommentResponse toResponse(Comment comment);

    Comment toEntity(CommentCreateRequest request);

    void update(
            CommentUpdateRequest request,
            @MappingTarget Comment comment
    );
}
