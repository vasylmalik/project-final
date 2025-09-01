package com.javarush.jira.bugtracking.task.internal.web;

import com.javarush.jira.bugtracking.task.TaskService;
import com.javarush.jira.bugtracking.task.to.TaskTagsTo;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/tasks/{taskId}/tags")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
public class TaskTagRestController {
    private final TaskService taskService;

    public TaskTagRestController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addMany(@PathVariable long taskId, @RequestBody @Valid TaskTagsTo to) {
        taskService.addTagsToTask(taskId, to.names());
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void replaceAll(@PathVariable long taskId, @RequestBody @Valid TaskTagsTo to) {
        taskService.setTaskTags(taskId, to.names());
    }

    @DeleteMapping("/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOne(@PathVariable long taskId, @PathVariable String name) {
        taskService.removeTagFromTask(taskId, name);
    }

    @GetMapping
    public Set<String> list(@PathVariable long taskId) {
        return taskService.get(taskId).getTags();
    }
}
