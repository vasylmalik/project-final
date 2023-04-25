package com.javarush.jira.bugtracking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = TaskController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class TaskController {
    static final String REST_URL = "/api/tasks";
    private TaskService service;

    @GetMapping("/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public String addTag(@PathVariable Long taskId, @RequestParam String tag) {
        log.debug("add tag={} for taskId={}", tag, taskId);
        service.addTag(taskId, tag);

        return "index";
    }
}
