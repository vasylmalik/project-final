package com.javarush.jira.bugtracking.sprint;

import com.javarush.jira.bugtracking.Handlers;
import com.javarush.jira.bugtracking.project.ProjectRepository;
import com.javarush.jira.bugtracking.sprint.to.SprintTo;
import com.javarush.jira.common.BaseHandler;
import com.javarush.jira.common.util.Util;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.javarush.jira.bugtracking.ObjectType.SPRINT;
import static com.javarush.jira.ref.RefType.SPRINT_STATUS;
import static com.javarush.jira.ref.ReferenceService.getRefs;

@Slf4j
@Controller
@RequestMapping(BaseHandler.UI_URL)
@RequiredArgsConstructor
public class SprintUIController {
    private final SprintMapperFull mapperFull;

    private final Handlers.SprintHandler handler;
    private final Handlers.AttachmentHandler attachmentHandler;

    private final ProjectRepository projectRepository;

    @GetMapping("/sprints/{id}")
    public String get(@PathVariable long id, @RequestParam(required = false) boolean fragment, Model model) {
        log.info("get {}", id);
        model.addAttribute("sprint", mapperFull.toTo(Util.checkExist(id, handler.getRepository().findFullById(id))));
        model.addAttribute("fragment", fragment);
        model.addAttribute("attachs", attachmentHandler.getRepository().getAllForObject(id, SPRINT));
        model.addAttribute("belongs", handler.getAllBelongs(id));
        return "sprint";
    }

    @GetMapping("/mngr/sprints/edit/{id}")
    public String edit(@PathVariable long id, Model model) {
        log.info("edit {}", id);
        model.addAttribute("sprint", handler.getTo(id));
        model.addAttribute("statuses", getRefs(SPRINT_STATUS));
        model.addAttribute("attachs", attachmentHandler.getRepository().getAllForObject(id, SPRINT));
        return "sprint-edit";
    }

    @GetMapping("/mngr/sprints/new")
    public String editFormNew(@RequestParam long projectId, Model model) {
        log.info("editFormNew for sprint with project {}", projectId);
        Sprint newSprint = new Sprint();
        newSprint.setProjectId(projectId);
        model.addAttribute("sprint", handler.getMapper().toTo(newSprint));
        model.addAttribute("statuses", getRefs(SPRINT_STATUS));
        return "sprint-edit";
    }

    @PostMapping("/mngr/sprints")
    public String createOrUpdate(@Valid @ModelAttribute("sprint") SprintTo sprintTo, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("statuses", getRefs(SPRINT_STATUS));
            if (!sprintTo.isNew()) {
                model.addAttribute("attachs", attachmentHandler.getRepository().getAllForObject(sprintTo.id(), SPRINT));
            }
            return "sprint-edit";
        }
        Long id = sprintTo.getId();
        if (sprintTo.isNew()) {
            id = handler.createWithBelong(sprintTo, SPRINT, "sprint_author").id();
        } else {
            handler.updateFromTo(sprintTo, sprintTo.id());
        }
        return "redirect:/ui/sprints/" + id;
    }
}
