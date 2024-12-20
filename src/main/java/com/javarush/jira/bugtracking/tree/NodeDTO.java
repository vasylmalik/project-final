package com.javarush.jira.bugtracking.tree;

import com.javarush.jira.bugtracking.ObjectType;
import com.javarush.jira.common.HasIdAndParentId;
import com.javarush.jira.common.to.CodeDTO;
import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Getter
public class NodeDTO extends CodeDTO implements HasIdAndParentId {
    @NonNull
    protected ObjectType type;
    @Nullable
    protected Long parentId;

    public NodeDTO(long id, @NonNull String code, @NonNull ObjectType type, Long parentId) {
        super(id, code);
        this.type = type;
        this.parentId = parentId;
    }
}
