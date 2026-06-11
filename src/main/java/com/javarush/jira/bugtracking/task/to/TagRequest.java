package com.javarush.jira.bugtracking.task.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagRequest {

    @NotBlank(message = "Tag cannot be empty")
    @Size(min = 2, max = 32, message = "Tag must be between 2 and 32 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$",
            message = "Tag can only contain letters, numbers, underscores and hyphens")
    private String tag;

    // Метод getTag() автоматически создается аннотацией @Data
    // Но если Lombok не работает, добавьте вручную:
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}