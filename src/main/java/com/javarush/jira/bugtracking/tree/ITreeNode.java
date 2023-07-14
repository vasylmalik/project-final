package com.javarush.jira.bugtracking.tree;

import com.javarush.jira.common.HasIdAndParentId;

import java.util.List;

public interface ITreeNode<T extends HasIdAndParentId, R extends ITreeNode<T, R>> {
    List<R> subNodes();
}
