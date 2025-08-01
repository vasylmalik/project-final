package com.javarush.jira.bugtracking.report;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = ReportRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class ReportRestController {
    static final String REST_URL = "/api/reports/{sprintId}";

    private final ReportRepository reportRepository;

    @GetMapping
    @Operation(
            summary = "Отримати звіт по задачах спринту",
            description = "Повертає список зведеної інформації про задачі у межах вказаного спринту, включаючи кількість, статуси та інші ключові метрики"
    )
    public List<TaskSummary> getTaskSummaries(@PathVariable long sprintId) {
        log.info("get task summaries for sprint with id={}", sprintId);
        return reportRepository.getTaskSummaries(sprintId);
    }
}
