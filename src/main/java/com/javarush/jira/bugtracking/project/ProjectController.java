package com.javarush.jira.bugtracking.project;

import com.javarush.jira.bugtracking.Handlers;
import static com.javarush.jira.bugtracking.ObjectType.PROJECT;
import com.javarush.jira.bugtracking.project.to.ProjectTo;
import static com.javarush.jira.common.BaseHandler.REST_URL;
import static com.javarush.jira.common.BaseHandler.createdResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ProjectController {
    private final Handlers.ProjectHandler handler;

    @GetMapping("/projects")
    public List<ProjectTo> getAll() {
        return handler.getAllTos(ProjectRepository.NEWEST_FIRST);
    }

    @GetMapping("/projects/{id}")
    public ProjectTo getById(@PathVariable Long id) {
        return handler.getTo(id);
    }

    @PostMapping(path = "/mngr/projects", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Project> create(@Valid @RequestBody ProjectTo projectTo) {
        Project created = handler.createWithBelong(projectTo, PROJECT, "project_author");
        return createdResponse(REST_URL + "/projects", created);
    }

    @PutMapping("/mngr/projects/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody ProjectTo projectTo, @PathVariable Long id) {
        handler.updateFromTo(projectTo, id);
    }

    @PatchMapping("/mngr/projects/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable long id, @RequestParam boolean enabled) {
        handler.enable(id, enabled);
    }
}
