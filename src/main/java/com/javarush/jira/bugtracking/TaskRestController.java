package com.javarush.jira.bugtracking;

import com.javarush.jira.bugtracking.internal.model.Task;
import com.javarush.jira.bugtracking.internal.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(TaskRestController.REST_URL)
@AllArgsConstructor
public class TaskRestController {

    static final String REST_URL = "/api/task";
    private final TaskService taskService;
    private final TaskRepository repository;

    @PutMapping("/add_tag/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void addTag(@PathVariable long id, @RequestParam String tag){
       taskService.addTag(tag, id);
    }
}
