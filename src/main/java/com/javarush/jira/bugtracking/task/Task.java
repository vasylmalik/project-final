package com.javarush.jira.bugtracking.task;

import com.javarush.jira.bugtracking.project.Project;
import com.javarush.jira.bugtracking.sprint.Sprint;
import com.javarush.jira.common.HasCode;
import com.javarush.jira.common.model.TitleEntity;
import com.javarush.jira.common.util.validation.Code;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;
import java.util.Set;

import static com.javarush.jira.bugtracking.task.TaskUtil.checkStatusChangePossible;

@Entity
@Table(name = "task")
@Getter
@Setter
@NoArgsConstructor
public class Task extends TitleEntity implements HasCode {
    // title, typeCode, statusCode duplicated here and in Activity for sql simplicity

    // link to Reference.code with RefType.TASK
    @Code
    @Column(name = "type_code", nullable = false)
    private String typeCode;

    // link to Reference.code with RefType.TASK_STATUS
    @Code
    @Column(name = "status_code", nullable = false)
    private String statusCode;

    //    https://stackoverflow.com/a/44539145/548473
    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Task parent;

    @Column(name = "parent_id")
    private Long parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false, insertable = false, updatable = false)
    private Project project;

    @Column(name = "project_id")
    private long projectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_id", insertable = false, updatable = false)
    private Sprint sprint;

    @Column(name = "sprint_id")
    private Long sprintId;

    @CollectionTable(name = "task_tag",
            joinColumns = @JoinColumn(name = "task_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"task_id", "tag"}, name = "uk_task_tag"))
    @Column(name = "tag")
    @ElementCollection(fetch = FetchType.EAGER)
    @JoinColumn()
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<@Size(min = 2, max = 32) String> tags = Set.of();

    //  history of comments and task fields changing
    @OneToMany(mappedBy = "taskId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Activity> activities;

    public Task(Long id, String title, String typeCode, String statusCode, Long parentId, long projectId, Long sprintId) {
        super(id, title);
        this.typeCode = typeCode;
        this.statusCode = statusCode;
        this.parentId = parentId;
        this.projectId = projectId;
        this.sprintId = sprintId;
    }

    public void checkAndSetStatusCode(String statusCode) {
        checkStatusChangePossible(this.statusCode, statusCode);
        this.statusCode = statusCode;
    }

    @Override
    public String getCode() {
        return typeCode + '-' + id;
    }
}
