package com.javarush.jira.bugtracking.task;

import com.javarush.jira.bugtracking.Handlers;
import com.javarush.jira.bugtracking.ObjectType;
import com.javarush.jira.bugtracking.attachment.AttachmentRepository;
import com.javarush.jira.bugtracking.task.to.ActivityTo;
import com.javarush.jira.bugtracking.task.to.TaskToExt;
import com.javarush.jira.bugtracking.task.to.TaskToFull;
import com.javarush.jira.ref.RefTo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.javarush.jira.ref.RefType.*;
import static com.javarush.jira.ref.ReferenceService.getRefs;

@Slf4j
@Controller
@RequestMapping(TaskUIController.TASK_URL)
@RequiredArgsConstructor
public class TaskUIController {
    static final String TASK_URL = "/ui/tasks";

    private final TaskService service;
    private final AttachmentRepository attachmentRepository;
    private final Handlers.ActivityHandler activityHandler;
    private final Handlers.AttachmentHandler attachmentHandler;
    private final Handlers.TaskHandler taskHandler;

    @GetMapping("/{id}")
    public String get(@PathVariable long id, @RequestParam(required = false) boolean fragment, Model model) {
        log.info("get {}", id);
        TaskToFull taskTo = service.get(id);
        addTaskInfo(model, taskTo);
        model.addAttribute("fragment", fragment);
        model.addAttribute("belongs", taskHandler.getAllBelongs(id));
        return "task";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable long id, Model model) {
        log.info("show edit form for task {}", id);
        TaskToFull taskTo = service.get(id);
        addTaskInfo(model, taskTo);
        addRefs(model, taskTo.getStatusCode());
        return "task-edit";
    }

    @GetMapping(value = "/new", params = "sprintId")
    public String editFormNew(@RequestParam long sprintId, Model model) {
        log.info("show edit form for new task with sprint {}", sprintId);
        TaskToExt newTask = service.getNewWithSprint(sprintId);
        addNewTaskInfoAndRefs(newTask, model);
        return "task-edit";
    }

    @GetMapping(value = "/new", params = "projectId")
    public String editFormNewInBacklog(@RequestParam long projectId, Model model) {
        log.info("show edit form for new task with project {}", projectId);
        TaskToExt newTask = service.getNewWithProject(projectId);
        addNewTaskInfoAndRefs(newTask, model);
        return "task-edit";
    }

    @GetMapping(value = "/new", params = "parentId")
    public String editFormNewSubTask(@RequestParam long parentId, Model model) {
        log.info("show edit form for new subtask of {}", parentId);
        TaskToExt newTask = service.getNewWithParent(parentId);
        addNewTaskInfoAndRefs(newTask, model);
        return "task-edit";
    }

    @PostMapping
    public String createOrUpdate(@Valid @ModelAttribute("task") TaskToExt taskTo, BindingResult result, Model model) {
        if (result.hasErrors()) {
            addRefs(model, taskTo.getStatusCode());
            List<ActivityTo> activityTos = taskTo.isNew() ? Collections.emptyList() :
                    activityHandler.getMapper().toToList(activityHandler.getRepository().findAllByTaskIdOrderByUpdatedDesc(taskTo.getId()));
            List<ActivityTo> comments = getComments(activityTos);
            activityTos.removeAll(comments);
            model.addAttribute("comments", comments);
            model.addAttribute("activities", activityTos);
            if (!taskTo.isNew()) {
                model.addAttribute("attachs", attachmentHandler.getRepository().getAllForObject(taskTo.id(), ObjectType.TASK));
            }
            return "task-edit";
        }
        Long taskId = taskTo.getId();
        if (taskTo.isNew()) {
            log.info("create {}", taskTo);
            Task created = service.create(taskTo);
            taskId = created.id();
        } else {
            log.info("update {} with id={}", taskTo, taskTo.id());
            service.update(taskTo, taskTo.id());
        }
        return "redirect:/ui/tasks/" + taskId;
    }

    private void addTaskInfo(Model model, TaskToFull taskTo) {
        List<ActivityTo> comments = getComments(taskTo.getActivityTos());
        taskTo.getActivityTos().removeAll(comments);
        model.addAttribute("task", taskTo);
        model.addAttribute("comments", comments);
        model.addAttribute("attachs", attachmentHandler.getRepository().getAllForObject(taskTo.id(), ObjectType.TASK));
        model.addAttribute("activities", taskTo.getActivityTos());
    }

    private void addRefs(Model model, String currentStatus) {
        model.addAttribute("types", getRefs(TASK));
        model.addAttribute("statuses", getPossibleStatusRefs(currentStatus));
        model.addAttribute("priorities", getRefs(PRIORITY));
    }

    private void addNewTaskInfoAndRefs(TaskToExt newTask, Model model) {
        model.addAttribute("task", newTask);
        model.addAttribute("types", getRefs(TASK));
        model.addAttribute("statuses", getRefs(TASK_STATUS));
        model.addAttribute("priorities", getRefs(PRIORITY));

    }

    private Map<String, RefTo> getPossibleStatusRefs(String currentStatus) {
        Set<String> possibleStatuses = new HashSet<>();
        possibleStatuses.add(currentStatus);
        Map<String, RefTo> taskStatusRefs = getRefs(TASK_STATUS);
        String possibleStatusesAux = taskStatusRefs.get(currentStatus).getAux(0);
        if (possibleStatusesAux != null) {
            possibleStatuses.addAll(Set.of(possibleStatusesAux.split(",")));
        }
        return taskStatusRefs.entrySet().stream()
                .filter(ref -> possibleStatuses.contains(ref.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (ref1, ref2) -> ref1, LinkedHashMap::new));
    }

    private List<ActivityTo> getComments(List<ActivityTo> activityTos) {
        return activityTos.stream()
                .filter(activity -> activity.getComment() != null)
                .toList();
    }
}
