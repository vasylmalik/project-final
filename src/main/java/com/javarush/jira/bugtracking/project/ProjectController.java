package com.javarush.jira.bugtracking.project;

import com.javarush.jira.bugtracking.Handlers;
import com.javarush.jira.bugtracking.project.to.ProjectDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.javarush.jira.bugtracking.ObjectType.PROJECT;
import static com.javarush.jira.common.BaseHandler.REST_URL;
import static com.javarush.jira.common.BaseHandler.createdResponse;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ProjectController {
    private final Handlers.ProjectHandler handler;

    @GetMapping("/projects")
    public List<ProjectDTO> getAll() {
        return handler.getAllDTOs(ProjectRepository.NEWEST_FIRST);
    }

    @GetMapping("/projects/{id}")
    public ProjectDTO getById(@PathVariable Long id) {
        return handler.getTo(id);
    }

    @PostMapping(path = "/mngr/projects", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Project> create(@Valid @RequestBody ProjectDTO projectDTO) {
        Project created = handler.createWithBelong(projectDTO, PROJECT, "project_author");
        return createdResponse(REST_URL + "/projects", created);
    }

    @PutMapping("/mngr/projects/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody ProjectDTO projectDTO, @PathVariable Long id) {
        handler.updateFromDTO(projectDTO, id);
    }

    @PatchMapping("/mngr/projects/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable long id, @RequestParam boolean enabled) {
        handler.enable(id, enabled);
    }
}
