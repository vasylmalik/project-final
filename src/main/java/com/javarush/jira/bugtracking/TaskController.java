package com.javarush.jira.bugtracking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping(value = TaskController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskController {
    public static final String REST_URL = "/api/tasks";

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{taskId}")
    public void addTags(@PathVariable("taskId") Optional<Long> taskId, @RequestParam List<String> tags) {
        if (taskId.isPresent() || tags.isEmpty()) {
            throw new IllegalArgumentException("Invalid path variable or request parameter.");
        }
        taskService.addTags(taskId.get(), tags);
    }
}
