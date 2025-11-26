package com.javarush.jira.bugtracking.task;

import com.javarush.jira.bugtracking.Handlers;
import com.javarush.jira.bugtracking.UserBelong;
import com.javarush.jira.bugtracking.UserBelongRepository;
import com.javarush.jira.bugtracking.task.to.ActivityTo;
import com.javarush.jira.bugtracking.task.to.TaskTo;
import com.javarush.jira.bugtracking.task.to.TaskToExt;
import com.javarush.jira.bugtracking.task.to.TaskToFull;
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
    public TaskToFull get(@PathVariable long id) {
        log.info("get task by id={}", id);
        return taskService.get(id);
    }

    @GetMapping("/by-sprint")
    public List<TaskTo> getAllBySprint(@RequestParam long sprintId) {
        log.info("get all for sprint {}", sprintId);
        return sortTasksAsTree(handler.getMapper().toToList(handler.getRepository().findAllBySprintId(sprintId)));
    }

    private List<TaskTo> sortTasksAsTree(List<TaskTo> tasks) {
        List<TaskTreeNode> roots = Util.makeTree(tasks, TaskTreeNode::new);
        List<TaskTo> sortedTasks = new ArrayList<>();
        roots.forEach(root -> {
            sortedTasks.add(root.taskTo);
            List<TaskTreeNode> subNodes = root.subNodes();
            LinkedList<TaskTreeNode> stack = new LinkedList<>(subNodes);
            while (!stack.isEmpty()) {
                TaskTreeNode node = stack.poll();
                sortedTasks.add(node.taskTo);
                node.subNodes().forEach(stack::addFirst);
            }
        });
        return sortedTasks;
    }

    @GetMapping("/by-project")
    public List<TaskTo> getAllByProject(@RequestParam long projectId) {
        log.info("get all for project {}", projectId);
        return handler.getMapper().toToList(handler.getRepository().findAllByProjectId(projectId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Task> createWithLocation(@Valid @RequestBody TaskToExt taskTo) {
        return createdResponse(REST_URL, taskService.create(taskTo));
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody TaskToExt taskTo, @PathVariable long id) {
        taskService.update(taskTo, id);
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
    public List<ActivityTo> getComments(@PathVariable long id) {
        log.info("get comments for task with id={}", id);
        return activityHandler.getMapper().toToList(activityHandler.getRepository().findAllComments(id));
    }

    @PostMapping(value = "/activities", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Activity create(@Valid @RequestBody ActivityTo activityTo) {
        return activityService.create(activityTo);
    }

    @PutMapping(path = "/activities/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody ActivityTo activityTo, @PathVariable long id) {
        activityService.update(activityTo, id);
    }

    @DeleteMapping("/activities/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        activityService.delete(id);
    }

    private record TaskTreeNode(TaskTo taskTo, List<TaskTreeNode> subNodes) implements ITreeNode<TaskTo, TaskTreeNode> {
        public TaskTreeNode(TaskTo taskTo) {
            this(taskTo, new LinkedList<>());
        }
    }

    @PostMapping("/{id}/tag")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addTag(@PathVariable long id, @RequestBody String tag) {
        taskService.addTag(id, tag);
    }
}
