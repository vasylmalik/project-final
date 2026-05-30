package com.javarush.jira.bugtracking.task.to;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class TagsTo {
    @NotNull
    private Set<@Size(min = 2, max = 32) String> tags = Set.of();

    public TagsTo(@Nullable Set<String> tags) {
        setTags(tags);
    }

    public void setTags(@Nullable Set<String> tags) {
        this.tags = tags == null ? Set.of() : Set.copyOf(tags);
    }
}
