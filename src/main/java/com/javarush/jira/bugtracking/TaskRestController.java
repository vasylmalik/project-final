package com.javarush.jira.bugtracking;

import com.javarush.jira.login.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(value = TaskRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class TaskRestController {
    static final String REST_URL = "/api/bugtracking/task";

    private final TaskService taskService;
    private final UserBelongService userBelongService;

    public TaskRestController(TaskService taskService, UserBelongService userBelongService) {
        this.taskService = taskService;
        this.userBelongService = userBelongService;
    }

    @PutMapping(path = "addtag", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addTag(@RequestParam Set<String> tags,
                       @RequestParam long taskId) {
        taskService.addTags(tags, taskId);
    }

    @PutMapping(path = "subscribe", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void subscribe(@RequestParam long taskId,
                          @RequestParam String userTypeCode,
                          @AuthenticationPrincipal AuthUser authUser) {
        userBelongService.subscribeToTask(authUser.getUser(), taskId, userTypeCode);
    }

}
