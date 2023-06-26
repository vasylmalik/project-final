package com.javarush.jira.bugtracking.project;

import com.javarush.jira.common.HasCode;
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
public class Project extends TitleEntity implements HasCode {

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
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Project parent;

    @Column(name = "parent_id")
    private Long parentId;

    public Project(Long id, String code, String title, String typeCode, String description, Long parentId) {
        super(id, title);
        this.code = code;
        this.parentId = parentId;
        this.description = description;
        this.typeCode = typeCode;
    }
}
