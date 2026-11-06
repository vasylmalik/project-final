package com.javarush.jira.bugtracking.report;

import com.javarush.jira.bugtracking.project.ProjectRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping(ReportUIController.REPORTS_URL)
public class ReportUIController {
    static final String REPORTS_URL = "/ui/reports";

    private final ProjectRepository projectRepository;

    @GetMapping
    public String showReportPage(Model model) {
        log.info("show sprint report page");
        model.addAttribute("projects", projectRepository.findAll());
        return "report";
    }
}
