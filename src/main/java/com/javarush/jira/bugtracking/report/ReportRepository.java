package com.javarush.jira.bugtracking.report;

import com.javarush.jira.bugtracking.task.Task;
import com.javarush.jira.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface ReportRepository extends BaseRepository<Task> {
    @Query("SELECT new com.javarush.jira.bugtracking.report.TaskSummary(r.title, COUNT(t.id)) " +
            "FROM Reference r LEFT JOIN Task t " +
            "ON r.code = t.statusCode AND t.sprint.id = :sprintId " +
            "WHERE r.refType = :#{T(com.javarush.jira.ref.RefType).TASK_STATUS} " +
            "GROUP BY r.title, r.id " +
            "ORDER BY r.id")
    List<TaskSummary> getTaskSummaries(long sprintId);
}
