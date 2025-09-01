package com.javarush.jira.bugtracking.task.to;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record TaskTagsTo(@NotEmpty Set<@Size(min = 2, max = 32) String> names) {}

