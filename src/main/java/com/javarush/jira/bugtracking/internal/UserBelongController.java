package com.javarush.jira.bugtracking.internal;

import com.javarush.jira.bugtracking.UserBelongService;
import com.javarush.jira.login.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = UserBelongController.REST_URL)
@AllArgsConstructor
@Slf4j
public class UserBelongController {
    static final String REST_URL = "/api/user-belong";

    private UserBelongService service;

    @GetMapping("/to")
    @Operation(summary = "Subscribe to task", description = "subscribe to task that`s not assigned to the current user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void subscribeToTask(@RequestParam long taskId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        long userId = ((AuthUser) authentication.getPrincipal()).id();

        log.debug("subscribe user with id={} to task with id={}", userId, taskId);
        service.subscribeToTask(userId, taskId);
    }
}
