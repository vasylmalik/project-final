package com.javarush.jira.bugtracking.project;

import com.javarush.jira.bugtracking.Handlers;
import com.javarush.jira.bugtracking.project.to.ProjectTo;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(
            summary = "Отримати список усіх проєктів",
            description = "Повертає список усіх проєктів у порядку від найновіших до найстаріших"
    )

    public List<ProjectTo> getAll() {
        return handler.getAllTos(ProjectRepository.NEWEST_FIRST);
    }

    @GetMapping("/projects/{id}")
    @Operation(
            summary = "Отримати проєкт за ID",
            description = "Повертає повну інформацію про проєкт із зазначеним ID"
    )
    public ProjectTo getById(@PathVariable Long id) {
        return handler.getTo(id);
    }

    @PostMapping(path = "/mngr/projects", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Створити новий проєкт",
            description = "Створює новий проєкт на основі переданих даних"
    )
    public ResponseEntity<Project> create(@Valid @RequestBody ProjectTo projectTo) {
        Project created = handler.createWithBelong(projectTo, PROJECT, "project_author");
        return createdResponse(REST_URL + "/projects", created);
    }

    @PutMapping("/mngr/projects/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Оновити проєкт",
            description = "Оновлює існуючий проєкт за його ID"
    )
    public void update(@Valid @RequestBody ProjectTo projectTo, @PathVariable Long id) {
        handler.updateFromTo(projectTo, id);
    }

    @PatchMapping("/mngr/projects/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Увімкнути/вимкнути проєкт",
            description = "Змінює статус активності проєкту: enabled = true/false"
    )
    public void enable(@PathVariable long id, @RequestParam boolean enabled) {
        handler.enable(id, enabled);
    }
}
