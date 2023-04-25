package com.javarush.jira.bugtracking.internal;

import com.javarush.jira.bugtracking.UserBelongService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = UserBelongController.REST_URL)
@AllArgsConstructor
@Slf4j
public class UserBelongController {
       static final String REST_URL = "/api/user-belong";

       private UserBelongService service;

       @GetMapping("/{userId}")
       @ResponseStatus(HttpStatus.NO_CONTENT)
       @Transactional
       public void subscribeToTask(@PathVariable long userId, @RequestParam long taskId) {
              log.debug("subscribe user with id={} to task with id={}", userId, taskId);
              service.subscribeToTask(userId, taskId);
       }
}
