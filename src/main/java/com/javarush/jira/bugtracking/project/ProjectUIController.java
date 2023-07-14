package com.javarush.jira.bugtracking.project;

import com.javarush.jira.bugtracking.Handlers;
import com.javarush.jira.bugtracking.ObjectType;
import com.javarush.jira.bugtracking.project.to.ProjectTo;
import com.javarush.jira.common.BaseHandler;
import com.javarush.jira.common.util.Util;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.javarush.jira.ref.RefType.PROJECT;
import static com.javarush.jira.ref.ReferenceService.getRefs;

@Slf4j
@Controller
@RequestMapping(BaseHandler.UI_URL)
@RequiredArgsConstructor
public class ProjectUIController {
    private final ProjectMapperFull mapperFull;

    private final Handlers.ProjectHandler handler;
    private final Handlers.AttachmentHandler attachmentHandler;

    @GetMapping("/projects/{id}")
    public String get(@PathVariable long id, @RequestParam(required = false) boolean fragment, Model model) {
        model.addAttribute("project", mapperFull.toTo(Util.checkExist(id, handler.getRepository().findFullById(id))));
        model.addAttribute("fragment", fragment);
        model.addAttribute("attachs", attachmentHandler.getRepository().getAllForObject(id, ObjectType.PROJECT));
        model.addAttribute("belongs", handler.getAllBelongs(id));
        return "project";
    }

    @GetMapping("/mngr/projects/edit/{id}")
    public String edit(@PathVariable long id, Model model) {
        log.info("edit {}", id);
        model.addAttribute("project", handler.getTo(id));
        model.addAttribute("types", getRefs(PROJECT));
        model.addAttribute("attachs", attachmentHandler.getRepository().getAllForObject(id, ObjectType.PROJECT));
        return "project-edit";
    }

    @GetMapping("/mngr/projects/new")
    public String editFormNew(@RequestParam(required = false) Long parentId, Model model) {
        log.info("editFormNew for task with parentId {}", parentId);
        Project newProject = new Project();
        newProject.setParentId(parentId);
        model.addAttribute("project", handler.getMapper().toTo(newProject));
        model.addAttribute("types", getRefs(PROJECT));
        return "project-edit";
    }

    @PostMapping("/mngr/projects")
    public String createOrUpdate(@Valid @ModelAttribute("project") ProjectTo projectTo, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("types", getRefs(PROJECT));
            if (!projectTo.isNew()) {
                model.addAttribute("attachs", attachmentHandler.getRepository().getAllForObject(projectTo.id(), ObjectType.PROJECT));
            }
            return "project-edit";
        }
        Long id = projectTo.getId();
        if (projectTo.isNew()) {
            Project project = handler.createWithBelong(projectTo, ObjectType.PROJECT, "project_author");
            id = project.getId();
        } else {
            handler.updateFromTo(projectTo, projectTo.id());
        }
        return "redirect:/ui/projects/" + id;
    }
}
