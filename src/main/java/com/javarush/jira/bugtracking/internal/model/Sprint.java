package com.javarush.jira.bugtracking.internal.model;

import com.javarush.jira.common.model.TitleEntity;
import com.javarush.jira.common.util.validation.Code;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "sprint")
@Getter
@Setter
@NoArgsConstructor
public class Sprint extends TitleEntity {

    // link to Reference.code with RefType.SPRINT_STATUS
    @Code
    @Column(name = "status_code", nullable = false)
    private String statusCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Project project;
}
