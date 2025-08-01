package com.javarush.jira.bugtracking.tree;


import com.javarush.jira.bugtracking.project.ProjectMapper;
import com.javarush.jira.bugtracking.project.ProjectRepository;
import com.javarush.jira.bugtracking.sprint.SprintMapper;
import com.javarush.jira.bugtracking.sprint.SprintRepository;
import com.javarush.jira.bugtracking.sprint.to.SprintTo;
import com.javarush.jira.bugtracking.task.TaskRepository;
import com.javarush.jira.bugtracking.task.mapper.TaskMapper;
import com.javarush.jira.common.util.Util;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = TreeRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class TreeRestController {
    static final String REST_URL = "/api/tree";

    private final NodeMapper mapper;
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final SprintRepository sprintRepository;
    private final SprintMapper sprintMapper;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    private static <T> List<TreeNode> toTree(List<T> list, Function<T, NodeTo> mapper) {
        List<NodeTo> nodes = list.stream()
                .map(mapper)
                .toList();
        return Util.makeTree(nodes, TreeNode::new);
    }

    @GetMapping("/projects")
    @Operation(
            summary = "Отримати дерево проєктів",
            description = "Повертає всі проєкти у вигляді ієрархічного дерева NodeTo"
    )
    public List<TreeNode> getProjects() {
        log.info("get projects tree");
        return toTree(projectMapper.toToList(projectRepository.getAll()), mapper::fromProject);
    }

    @GetMapping("/projects/{projectId}/sprints")
    @Operation(
            summary = "Отримати спринти проєкту та беклог",
            description = "Повертає список спринтів проєкту у вигляді дерева, включаючи окремий вузол 'Backlog'"
    )
    public List<TreeNode> getSprintsAndBacklog(@PathVariable long projectId) {
        log.info("get project {} sprints", projectId);
        List<SprintTo> sprintTos = new ArrayList<>(sprintMapper.toToList(sprintRepository.getAllByProject(projectId)));
        sprintTos.add(new SprintTo(0L, "Backlog", null, projectId));
        return toTree(sprintTos, mapper::fromSprint);
    }

    @GetMapping("/sprints/{sprintId}/tasks")
    @Operation(
            summary = "Отримати задачі для спринту",
            description = "Повертає задачі, прикріплені до певного спринту, у вигляді дерева"
    )
    public List<TreeNode> getSprintTasks(@PathVariable long sprintId) {
        log.info("get sprint {} tasks", sprintId);
        return toTree(taskMapper.toToList(taskRepository.findAllBySprintId(sprintId)), mapper::fromTask);
    }

    @GetMapping("/projects/{projectId}/backlog/tasks")
    @Operation(
            summary = "Отримати задачі беклогу проєкту",
            description = "Повертає задачі проєкту, які ще не додані до жодного спринту, у вигляді дерева"
    )
    public List<TreeNode> getBacklogTasks(@PathVariable long projectId) {
        log.info("get project {} backlog tasks", projectId);
        return toTree(taskMapper.toToList(taskRepository.findAllByProjectIdAndSprintIsNull(projectId)), mapper::fromTask);
    }
}
