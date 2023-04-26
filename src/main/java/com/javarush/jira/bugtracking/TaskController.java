package com.javarush.jira.bugtracking;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = TaskController.REST_URL)
@AllArgsConstructor
@Slf4j
public class TaskController {
    static final String REST_URL = "/api/tasks";
    private TaskService service;

    @GetMapping("/{taskId}")
    @Operation(summary = "Add tag to task", description = "Add tag to task")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String addTag(@PathVariable Long taskId, @RequestParam String tag) {
        log.debug("add tag={} for taskId={}", tag, taskId);
        service.addTag(taskId, tag);

        return "index";
    }
}
