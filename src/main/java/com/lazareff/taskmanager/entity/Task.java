package com.lazareff.taskmanager.entity;

import com.lazareff.taskmanager.enums.TaskPriority;
import com.lazareff.taskmanager.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tasks")
@Getter
@Setter
public class Task extends AuditableEntity {

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

}
