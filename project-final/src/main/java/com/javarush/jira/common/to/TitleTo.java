package com.javarush.jira.common.to;

import com.javarush.jira.common.util.validation.Title;
import lombok.Getter;
import lombok.Setter;

@Getter
public class TitleTo extends CodeTo {
    @Title
    @Setter
    protected String title;

    public TitleTo(Long id, String code, String title) {
        super(id, code);
        this.title = title;
    }
}
