package com.javarush.jira.bugtracking.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.javarush.jira.common.model.TitleEntity;
import com.javarush.jira.common.util.validation.Code;
import com.javarush.jira.common.util.validation.Description;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "task")
@Getter
@Setter
@NoArgsConstructor
public class Task extends TitleEntity {

    // link to Reference.code with RefType.TASK
    @Code
    @Column(name = "type_code", nullable = false)
    private String typeCode;

    @Code
    @Column(name = "status_code", nullable = false)
    private String statusCode;

    @Code
    @Column(name = "priority_code", nullable = false)
    private String priorityCode;

    @Description
    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_id")
    private Sprint sprint;

    @Column(name = "updated")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updated;

    @Column(name = "estimate")
    @Nullable
    @Positive
    private Integer estimate;

    @CollectionTable(name = "task_tag",
            joinColumns = @JoinColumn(name = "task_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"task_id", "tag"}, name = "uk_task_tag"))
    @Column(name = "tag")
    @ElementCollection(fetch = FetchType.EAGER)
    @JoinColumn()
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<@Size(min = 2, max = 32) String> tags = Set.of();

    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
    private List<Activity> activities;

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Task parent;
}
