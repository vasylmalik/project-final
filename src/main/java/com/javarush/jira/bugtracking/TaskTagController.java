package com.javarush.jira.bugtracking;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(TaskTagController.REST_URL)
@AllArgsConstructor
public class TaskTagController {

    public static final String REST_URL = "api/tasks";
    private TaskService taskService;

    @PostMapping("/{taskId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void addTagToTask(@PathVariable Long taskId, @RequestParam String tag) {
        taskService.saveTag(taskId, tag);

    }
}