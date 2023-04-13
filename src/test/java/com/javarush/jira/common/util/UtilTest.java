package com.javarush.jira.common.util;

import com.javarush.jira.bugtracking.to.ProjectTo;
import com.javarush.jira.common.to.TreeNode;
import org.junit.jupiter.api.Test;

import java.util.List;

class UtilTest {

    @Test
    void makeTree() {
        ProjectTo a = new ProjectTo(1L, "A", true, "A", "A", "A", null);
        ProjectTo b = new ProjectTo(1L, "B", true, "B", "B", "B", a);
        ProjectTo c = new ProjectTo(1L, "C", true, "C", "C", "C", a);
        ProjectTo d = new ProjectTo(1L, "D", true, "D", "D", "D", b);
        ProjectTo e = new ProjectTo(1L, "E", true, "E", "E", "E", d);
        List<ProjectTo> projects = List.of(e, b, c, d, a);

        List<TreeNode<ProjectTo>> nodes = Util.makeTree(projects);
        for (TreeNode<ProjectTo> treeNode : nodes) {
            System.out.println(treeNode);
        }
    }
}