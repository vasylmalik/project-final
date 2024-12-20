package com.javarush.jira.bugtracking.task;

import com.javarush.jira.bugtracking.Handlers;
import com.javarush.jira.bugtracking.ObjectType;
import com.javarush.jira.bugtracking.attachment.AttachmentRepository;
import com.javarush.jira.bugtracking.task.to.ActivityDTO;
import com.javarush.jira.bugtracking.task.to.TaskDTOExt;
import com.javarush.jira.bugtracking.task.to.TaskDTOFull;
import com.javarush.jira.ref.RefDTO;
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
        TaskDTOFull taskDTOFull = service.get(id);
        addTaskInfo(model, taskDTOFull);
        model.addAttribute("fragment", fragment);
        model.addAttribute("belongs", taskHandler.getAllBelongs(id));
        return "task";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable long id, Model model) {
        log.info("show edit form for task {}", id);
        TaskDTOFull taskDTOFull = service.get(id);
        addTaskInfo(model, taskDTOFull);
        addRefs(model, taskDTOFull.getStatusCode());
        return "task-edit";
    }

    @GetMapping(value = "/new", params = "sprintId")
    public String editFormNew(@RequestParam long sprintId, Model model) {
        log.info("show edit form for new task with sprint {}", sprintId);
        TaskDTOExt newTask = service.getNewWithSprint(sprintId);
        addNewTaskInfoAndRefs(newTask, model);
        return "task-edit";
    }

    @GetMapping(value = "/new", params = "projectId")
    public String editFormNewInBacklog(@RequestParam long projectId, Model model) {
        log.info("show edit form for new task with project {}", projectId);
        TaskDTOExt newTask = service.getNewWithProject(projectId);
        addNewTaskInfoAndRefs(newTask, model);
        return "task-edit";
    }

    @GetMapping(value = "/new", params = "parentId")
    public String editFormNewSubTask(@RequestParam long parentId, Model model) {
        log.info("show edit form for new subtask of {}", parentId);
        TaskDTOExt newTask = service.getNewWithParent(parentId);
        addNewTaskInfoAndRefs(newTask, model);
        return "task-edit";
    }

    @PostMapping
    public String createOrUpdate(@Valid @ModelAttribute("task") TaskDTOExt taskDTOExt, BindingResult result, Model model) {
        if (result.hasErrors()) {
            addRefs(model, taskDTOExt.getStatusCode());
            List<ActivityDTO> activityDTOs = taskDTOExt.isNew() ? Collections.emptyList() :
                    activityHandler.getMapper().toDTOList(activityHandler.getRepository().findAllByTaskIdOrderByUpdatedDesc(taskDTOExt.getId()));
            List<ActivityDTO> comments = getComments(activityDTOs);
            activityDTOs.removeAll(comments);
            model.addAttribute("comments", comments);
            model.addAttribute("activities", activityDTOs);
            if (!taskDTOExt.isNew()) {
                model.addAttribute("attachs", attachmentHandler.getRepository().getAllForObject(taskDTOExt.id(), ObjectType.TASK));
            }
            return "task-edit";
        }
        Long taskId = taskDTOExt.getId();
        if (taskDTOExt.isNew()) {
            log.info("create {}", taskDTOExt);
            Task created = service.create(taskDTOExt);
            taskId = created.id();
        } else {
            log.info("update {} with id={}", taskDTOExt, taskDTOExt.id());
            service.update(taskDTOExt, taskDTOExt.id());
        }
        return "redirect:/ui/tasks/" + taskId;
    }

    private void addTaskInfo(Model model, TaskDTOFull taskDTOFull) {
        List<ActivityDTO> comments = getComments(taskDTOFull.getActivityDTOs());
        taskDTOFull.getActivityDTOs().removeAll(comments);
        model.addAttribute("task", taskDTOFull);
        model.addAttribute("comments", comments);
        model.addAttribute("attachs", attachmentHandler.getRepository().getAllForObject(taskDTOFull.id(), ObjectType.TASK));
        model.addAttribute("activities", taskDTOFull.getActivityDTOs());
    }

    private void addRefs(Model model, String currentStatus) {
        model.addAttribute("types", getRefs(TASK));
        model.addAttribute("statuses", getPossibleStatusRefs(currentStatus));
        model.addAttribute("priorities", getRefs(PRIORITY));
    }

    private void addNewTaskInfoAndRefs(TaskDTOExt newTask, Model model) {
        model.addAttribute("task", newTask);
        model.addAttribute("types", getRefs(TASK));
        model.addAttribute("statuses", getRefs(TASK_STATUS));
        model.addAttribute("priorities", getRefs(PRIORITY));

    }

    private Map<String, RefDTO> getPossibleStatusRefs(String currentStatus) {
        Set<String> possibleStatuses = new HashSet<>();
        possibleStatuses.add(currentStatus);
        Map<String, RefDTO> taskStatusRefs = getRefs(TASK_STATUS);
        String possibleStatusesAux = taskStatusRefs.get(currentStatus).getAux(0);
        if (possibleStatusesAux != null) {
            possibleStatuses.addAll(Set.of(possibleStatusesAux.split(",")));
        }
        return taskStatusRefs.entrySet().stream()
                .filter(ref -> possibleStatuses.contains(ref.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (ref1, ref2) -> ref1, LinkedHashMap::new));
    }

    private List<ActivityDTO> getComments(List<ActivityDTO> activityDTOs) {
        return activityDTOs.stream()
                .filter(activity -> activity.getComment() != null)
                .toList();
    }
}
