package com.javarush.jira.bugtracking;

import com.javarush.jira.bugtracking.to.TaskTo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskUIController {

    private final TaskService taskService;

    @PostMapping("/{id}/tags") //TODO: 6. Add feature new tags
    public String addTagToTask(@PathVariable("id") Long taskId, @RequestBody String[] tagsFrom) {
        Set<String> tags = Set.of(tagsFrom);
        taskService.addTagsToTask(taskId, tags);
        return "redirect:/";
    }

    @PostMapping("/{id}/users/{userId}") //TODO: 7. Add subscribe feature
    public String addUserToTask(@PathVariable("id") Long taskId, @PathVariable("userId") Long userId) {
        taskService.addUserToTask(taskId, userId);
        return "redirect:/";
    }

    @GetMapping("/backlog") //TODO: 12.add backlog
    public String getBacklog(Model model,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "3") int size) {
        Page<TaskTo> taskPage = taskService.getAllWhereSprintIsNull(page, size);

        model.addAttribute("tasks", taskPage.getContent());
        model.addAttribute("currentPage", taskPage.getNumber() + 1);
        model.addAttribute("totalItems", taskPage.getTotalElements());
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("pageSize", size);

        return "backlog";
    }

    @GetMapping("/{id}/summary") //TODO: 8.add task summary (task 10 with activity)
    public String getSummary(Model model, @PathVariable("id") Long taskId) {
        Map<String, String> taskSummary = taskService.getTaskSummary(taskId);
        model.addAttribute("taskId", taskId);
        if (!taskSummary.isEmpty()) {
            model.addAttribute("taskSummary", taskSummary);
        }
        return "summary";
    }
}
