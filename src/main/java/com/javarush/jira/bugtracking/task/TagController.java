package com.javarush.jira.bugtracking.task;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/*
    task 7 - completed
    Добавить новый функционал: добавления тегов к задаче (REST API + реализация на сервисе).
    Фронт делать необязательно. Таблица task_tag уже создана.
*/

@Slf4j
@Tag(name = "Tags", description = "API для работы с тегами")
@RestController
@RequestMapping(TaskController.REST_URL + "/{id}")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/get-tags")
    @Operation(
            summary = "Получить теги по ID задачи",
            description = "Возвращает набор тегов для указанной задачи.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Теги успешно получены"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    public Set<String> getTagsByTaskId(
            @Parameter(description = "Task id") @PathVariable long id
    ) {
        return tagService.getTags(id);
    }

    @PutMapping("/add-tags")
    @Operation(
            summary = "Добавить теги к задаче",
            description = "Добавляет новый набор тегов к указанной задаче.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Теги успешно добавлены"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос")
    })
    public ResponseEntity<?> addTags(
            @Parameter(description = "Task id") @PathVariable long id,
            @RequestBody @NotBlank Set<String> tags
    ) {
        var taskWithTags = tagService.addTags(id, tags);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskWithTags);
    }

    @PostMapping("/replace-tags")
    @Operation(
            summary = "Обновить теги задачи",
            description = "Обновляет набор тегов для указанной задачи.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Теги успешно обновлены"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос")
    })
    public ResponseEntity<?> replaceTags(
            @Parameter(description = "Task id") @PathVariable long id,
            @RequestBody @NotBlank Set<String> tags
    ) {
        var updatedTask = tagService.replaceTags(id, tags);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/delete-tags")
    @Operation(
            summary = "Удалить теги у задачи",
            description = "Удаляет все теги у указанной задачи.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Теги успешно удалены"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    public ResponseEntity<?> deleteTags(
            @Parameter(description = "Task id") @PathVariable long id
    ) {
        tagService.removeAllTags(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/delete-single-tag")
    @Operation(
            summary = "Удалить тег у задачи",
            description = "Удаляет одиночный тег у указанной задачи.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Тег успешно удален"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    public ResponseEntity<?> deleteSingleTag(
            @Parameter(description = "Task id") @PathVariable long id,
            @RequestBody @NotBlank String tag
    ) {
        tagService.removeSingleTag(id, tag);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
