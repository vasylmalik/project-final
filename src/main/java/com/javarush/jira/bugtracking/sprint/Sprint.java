package com.javarush.jira.bugtracking.sprint;

import com.javarush.jira.bugtracking.project.Project;
import com.javarush.jira.common.HasCode;
import com.javarush.jira.common.model.TimestampEntry;
import com.javarush.jira.common.util.validation.Code;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sprint")
@Getter
@Setter
@NoArgsConstructor
public class Sprint extends TimestampEntry implements HasCode {
    @Code
    @Column(name = "code", nullable = false)
    private String code;

    // link to Reference.code with RefType.SPRINT_STATUS
    @Code
    @Column(name = "status_code", nullable = false)
    private String statusCode;

    //    https://stackoverflow.com/a/44539145/548473
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false, insertable = false, updatable = false)
    private Project project;

    @Column(name = "project_id")
    private long projectId;

    public Sprint(Long id, String code, String statusCode, long projectId) {
        super(id);
        this.code = code;
        this.statusCode = statusCode;
        this.projectId = projectId;
    }
}
