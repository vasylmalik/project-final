package com.javarush.jira.bugtracking.internal.model;

import com.javarush.jira.common.model.TitleEntity;
import com.javarush.jira.common.util.validation.Code;
import com.javarush.jira.common.util.validation.Description;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "project")
@Getter
@Setter
@NoArgsConstructor
public class Project extends TitleEntity {

    @Code
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "description", nullable = false)
    @Description
    private String description;

    // link to Reference.code with RefType.PROJECT
    @Code
    @Column(name = "type_code", nullable = false)
    private String typeCode;

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Project parent;
}
