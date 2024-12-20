package com.javarush.jira.bugtracking.task;

import com.javarush.jira.bugtracking.Handlers;
import com.javarush.jira.bugtracking.UserBelong;
import com.javarush.jira.bugtracking.UserBelongRepository;
import com.javarush.jira.bugtracking.task.to.ActivityDTO;
import com.javarush.jira.bugtracking.task.to.TaskDTO;
import com.javarush.jira.bugtracking.task.to.TaskDTOExt;
import com.javarush.jira.bugtracking.task.to.TaskDTOFull;
import com.javarush.jira.bugtracking.tree.ITreeNode;
import com.javarush.jira.common.util.Util;
import com.javarush.jira.login.AuthUser;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.javarush.jira.common.BaseHandler.createdResponse;

@Slf4j
@RestController
@RequestMapping(value = TaskController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TaskController {

    public static final String REST_URL = "/api/tasks";

    private final TaskService taskService;
    private final ActivityService activityService;
    private final Handlers.TaskHandler handler;
    private final Handlers.ActivityHandler activityHandler;
    private final UserBelongRepository userBelongRepository;


    @GetMapping("/{id}")
    public TaskDTOFull get(@PathVariable long id) {
        log.info("get task by id={}", id);
        return taskService.get(id);
    }

    @GetMapping("/by-sprint")
    public List<TaskDTO> getAllBySprint(@RequestParam long sprintId) {
        log.info("get all for sprint {}", sprintId);
        return sortTasksAsTree(handler.getMapper().toDTOList(handler.getRepository().findAllBySprintId(sprintId)));
    }

    private List<TaskDTO> sortTasksAsTree(List<TaskDTO> tasks) {
        List<TaskTreeNode> roots = Util.makeTree(tasks, TaskTreeNode::new);
        List<TaskDTO> sortedTasks = new ArrayList<>();
        roots.forEach(root -> {
            sortedTasks.add(root.taskDTO);
            List<TaskTreeNode> subNodes = root.subNodes();
            LinkedList<TaskTreeNode> stack = new LinkedList<>(subNodes);
            while (!stack.isEmpty()) {
                TaskTreeNode node = stack.poll();
                sortedTasks.add(node.taskDTO);
                node.subNodes().forEach(stack::addFirst);
            }
        });
        return sortedTasks;
    }

    @GetMapping("/by-project")
    public List<TaskDTO> getAllByProject(@RequestParam long projectId) {
        log.info("get all for project {}", projectId);
        return handler.getMapper().toDTOList(handler.getRepository().findAllByProjectId(projectId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Task> createWithLocation(@Valid @RequestBody TaskDTOExt taskDTO) {
        return createdResponse(REST_URL, taskService.create(taskDTO));
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody TaskDTOExt taskDTOExt, @PathVariable long id) {
        taskService.update(taskDTOExt, id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable long id, @RequestParam boolean enabled) {
        handler.enable(id, enabled);
    }

    @PatchMapping("/{id}/change-status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeTaskStatus(@PathVariable long id, @NotBlank @RequestParam String statusCode) {
        log.info("change task(id={}) status to {}", id, statusCode);
        taskService.changeStatus(id, statusCode);
    }

    @PatchMapping("/{id}/change-sprint")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeTaskSprint(@PathVariable long id, @Nullable @RequestParam Long sprintId) {
        log.info("change task(id={}) sprint to {}", id, sprintId);
        taskService.changeSprint(id, sprintId);
    }

    @GetMapping("/assignments/by-sprint")
    public List<UserBelong> getTaskAssignmentsBySprint(@RequestParam long sprintId) {
        log.info("get task assignments for user {} for sprint {}", AuthUser.authId(), sprintId);
        return userBelongRepository.findActiveTaskAssignmentsForUserBySprint(AuthUser.authId(), sprintId);
    }

    @PatchMapping("/{id}/assign")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void assign(@PathVariable long id, @NotBlank @RequestParam String userType) {
        log.info("assign user {} as {} to task {}", AuthUser.authId(), userType, id);
        taskService.assign(id, userType, AuthUser.authId());
    }

    @PatchMapping("/{id}/unassign")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unAssign(@PathVariable long id, @NotBlank @RequestParam String userType) {
        log.info("unassign user {} as {} from task {}", AuthUser.authId(), userType, id);
        taskService.unAssign(id, userType, AuthUser.authId());
    }

    @GetMapping("/{id}/comments")
    public List<ActivityDTO> getComments(@PathVariable long id) {
        log.info("get comments for task with id={}", id);
        return activityHandler.getMapper().toDTOList(activityHandler.getRepository().findAllComments(id));
    }

    @PostMapping(value = "/activities", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Activity create(@Valid @RequestBody ActivityDTO activityDTO) {
        return activityService.create(activityDTO);
    }

    @PutMapping(path = "/activities/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody ActivityDTO activityDTO, @PathVariable long id) {
        activityService.update(activityDTO, id);
    }

    @DeleteMapping("/activities/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        activityService.delete(id);
    }

    private record TaskTreeNode(TaskDTO taskDTO, List<TaskTreeNode> subNodes) implements ITreeNode<TaskDTO, TaskTreeNode> {
        public TaskTreeNode(TaskDTO taskDTO) {
            this(taskDTO, new LinkedList<>());
        }
    }
}
