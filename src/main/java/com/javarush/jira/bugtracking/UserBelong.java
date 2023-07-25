package com.javarush.jira.bugtracking;

import com.javarush.jira.common.model.TimestampEntry;
import com.javarush.jira.common.util.validation.Code;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Link user with role to any object (Project, Task, Sprint)
@Entity
@Table(name = "user_belong",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"object_id", "object_type", "user_id", "user_type_code"}, name = "uk_user_belong")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserBelong extends TimestampEntry {

    // no FK, manual check
    @Column(name = "object_id", nullable = false)
    @NotNull
    private Long objectId;

    @Column(name = "object_type", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private ObjectType objectType;

    @Column(name = "user_id", nullable = false)
    @NotNull
    private Long userId;

    // link to Reference.code with RefType.USER_TYPE
    @Code
    @Column(name = "user_type_code", nullable = false)
    private String userTypeCode;
}
