package com.javarush.jira.bugtracking;

import com.javarush.jira.bugtracking.to.SprintTo;
import com.javarush.jira.bugtracking.to.TaskTo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping
public class DashboardUIController {

    private TaskService taskService;

    @GetMapping("/") // index page
    public String getAll(Model model) {
        List<TaskTo> tasks = taskService.getAll();
        Map<SprintTo, List<TaskTo>> taskMap = tasks.stream()
                .collect(Collectors.groupingBy(TaskTo::getSprint));
        model.addAttribute("taskMap", taskMap);
        return "index";
    }

    @PostMapping("/tasks/{id}/tags")
    public String addTagToTask(@PathVariable("id") Long taskId, @RequestBody String[] tagsFrom) {
        Set<String> tags = Set.of(tagsFrom);
        taskService.addTagsToTask(taskId, tags);
        return "redirect:/";
    }

    @PostMapping("/tasks/{id}/users/{userId}")
    public String addUserToTask(@PathVariable("id") Long taskId, @PathVariable("userId") Long userId) {
        taskService.addUserToTask(taskId, userId);
        return "redirect:/";
    }

    @GetMapping("/tasks/backlog")
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
}
