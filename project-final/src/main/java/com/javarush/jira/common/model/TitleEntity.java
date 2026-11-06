package com.javarush.jira.common.model;

import com.javarush.jira.common.util.validation.Title;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class TitleEntity extends TimestampEntry {

    @Title
    @Column(name = "title", nullable = false)
    protected String title;

    protected TitleEntity(Long id, String title) {
        super(id);
        this.title = title;
    }

    @Override
    public String toString() {
        return super.toString() + '[' + title + ']';
    }
}
