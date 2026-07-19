package com.lazareff.taskmanager.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentUpdateRequest {

    @NotBlank(message = "Comment cannot be blank")
    @Size(max = 3000, message = "Comment cannot be longer than 3000 characters")
    private String content;

}
