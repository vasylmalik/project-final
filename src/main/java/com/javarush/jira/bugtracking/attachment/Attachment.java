package com.javarush.jira.bugtracking.attachment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.javarush.jira.bugtracking.to.ObjectType;
import com.javarush.jira.common.model.NamedEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "attachment")
@Getter
@Setter
@NoArgsConstructor
public class Attachment extends NamedEntity {

    @Column(name = "file_link", nullable = false)
    private String fileLink;

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

    @Column(name = "date_time", columnDefinition = "timestamp default now()")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected LocalDateTime dateTime = LocalDateTime.now();

    public Attachment(Long id, String fileLink, Long objectId, ObjectType objectType, Long userId, String name) {
        super(id, name);
        this.fileLink = fileLink;
        this.objectId = objectId;
        this.objectType = objectType;
        this.userId = userId;
    }
}
