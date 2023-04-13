package com.javarush.jira.common.to;

import org.springframework.lang.NonNull;

import java.util.LinkedList;
import java.util.List;

public record TreeNode<T>(@NonNull T node, @NonNull List<TreeNode<T>> children) {
    public TreeNode(T node) {
        this(node, new LinkedList<>());
    }
}
