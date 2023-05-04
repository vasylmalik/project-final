package com.javarush.jira.bugtracking.internal.web;

import com.javarush.jira.bugtracking.internal.model.Task;
import com.javarush.jira.bugtracking.internal.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    TaskRepository taskRepository;

    @PostMapping(value = "taskTag", params = {"id","tag"})
    public ResponseEntity<Task> update(@RequestParam("id") long id, @RequestParam("tag") String tag){
        System.out.println(id);

        Optional<Task> taskOptional = taskRepository.findById(id);

        if (taskOptional.isPresent()){
            Task task = taskOptional.get();
            Set<String> tagSet = new HashSet<String>(Arrays.asList(tag));
            task.setTags(tagSet);
            return new ResponseEntity<>(taskRepository.save(task), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
