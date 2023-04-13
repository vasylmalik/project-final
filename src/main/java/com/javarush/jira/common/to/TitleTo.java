package com.javarush.jira.common.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.javarush.jira.common.util.validation.Title;
import lombok.Getter;
import lombok.Setter;

@Getter
public class TitleTo extends BaseTo {
    @Title
    @Setter
    protected String title;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    boolean enabled;

    public TitleTo(Long id, String title, boolean enabled) {
        super(id);
        this.title = title;
        this.enabled = enabled;
    }

    public void enable(boolean enabled) {
        this.enabled = enabled;
    }
}
