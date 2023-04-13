package com.javarush.jira.bugtracking.to;

import com.javarush.jira.common.to.TitleTo;
import jakarta.annotation.Nullable;
import lombok.Getter;

@Getter
public class NodeTo<E extends NodeTo<E>> extends TitleTo {
    @Nullable
    E parent;

    public NodeTo(Long id, String title, boolean enabled, E parent) {
        super(id, title, enabled);
        this.title = title;
        this.parent = parent;
    }
}
