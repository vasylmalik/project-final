package com.lazareff.taskmanager.dto.comment;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponse {

    private Long id;

    private String content;

    private Long userId;

    private Long taskId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
