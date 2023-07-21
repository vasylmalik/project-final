package com.javarush.jira.bugtracking.task;

import com.javarush.jira.common.model.BaseEntity;
import com.javarush.jira.common.util.validation.NoHtml;
import com.javarush.jira.login.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity")
@Getter
@Setter
@NoArgsConstructor
public class Activity extends BaseEntity implements HasAuthorId {
    @NoHtml
    @Size(max = 1024)
    @Nullable
    @Column(name = "title", nullable = false)
    protected String title;
    @NotNull
    @Column(name = "task_id")
    private Long taskId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", insertable = false, updatable = false)
    private User author;
    @NotNull
    @Column(name = "author_id")
    private Long authorId;
    @UpdateTimestamp
    @Nullable
    @Column(name = "updated")
    private LocalDateTime updated;
    @NoHtml
    @Size(max = 4096)
    @Nullable
    @Column(name = "comment")
    private String comment;
    // link to Reference.code with RefType.TASK_STATUS
    @NoHtml
    @Size(min = 2, max = 32)
    @Nullable
    @Column(name = "status_code")
    private String statusCode;
    // link to Reference.code with RefType.PRIORITY
    @NoHtml
    @Size(min = 2, max = 32)
    @Nullable
    @Column(name = "priority_code")
    private String priorityCode;
    // link to Reference.code with RefType.TASK
    @NoHtml
    @Size(min = 2, max = 32)
    @Nullable
    @Column(name = "type_code", nullable = false)
    private String typeCode;
    @NoHtml
    @Size(max = 4096)
    @Nullable
    @Column(name = "description")
    private String description;

    @Positive
    @Nullable
    @Column(name = "estimate")
    private Integer estimate;

    Activity(Long id, Long taskId, Long authorId) {
        super(id);
        this.taskId = taskId;
        this.authorId = authorId;
    }

    Activity(Long id, Long taskId, Long authorId, LocalDateTime updated, String comment, String statusCode, String priorityCode,
             String typeCode, String title, String description, Integer estimate) {
        super(id);
        this.taskId = taskId;
        this.authorId = authorId;
        this.updated = updated;
        this.comment = comment;
        this.statusCode = statusCode;
        this.priorityCode = priorityCode;
        this.typeCode = typeCode;
        this.title = title;
        this.description = description;
        this.estimate = estimate;
    }
}
