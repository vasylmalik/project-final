package com.javarush.jira.bugtracking;

import com.javarush.jira.login.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(value = TaskController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class TaskController {
    static final String REST_URL = "/api/bugtracking/task";

    @Autowired
    private TaskService taskService;
    @Autowired
    private UserBelongService userBelongService;

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
