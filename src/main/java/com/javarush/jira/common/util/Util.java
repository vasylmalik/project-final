package com.javarush.jira.common.util;

import com.javarush.jira.bugtracking.to.NodeTo;
import com.javarush.jira.common.to.TreeNode;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;
import org.jsoup.Jsoup;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class Util {
    public static <K, V> V getExisted(Map<K, V> map, K key) {
        return notNull(map.get(key), "Value with key {0} not found", key);
    }

    public static <T> T notNull(@Nullable T object, String message, Object... vars) {
        if (object == null) {
            throw new IllegalArgumentException(vars.length == 0 ? message : MessageFormat.format(message, vars));
        }
        return object;
    }

    @NotNull
    public static String getTitle(String template) {
        return notNull(Jsoup.parse(template).title(), "Template must has title as nonnull mail subject");
    }

    public static Map<String, Object> mergeMap(@NonNull Map<String, Object> map1, @Nullable Map<String, Object> map2) {
        if (CollectionUtils.isEmpty(map2)) return map1;
        if (CollectionUtils.isEmpty(map1)) return map2;
        Map<String, Object> result = new HashMap<>(map1);
        result.putAll(map2);
        return result;
    }

    public static <E extends NodeTo<E>> List<TreeNode<E>> makeTree(List<E> nodes) {
        List<TreeNode<E>> roots = new ArrayList<>();
        Map<E, TreeNode<E>> map = new HashMap<>();
        for (E node : nodes) {
            TreeNode<E> treeNode = map.computeIfAbsent(node, TreeNode::new);
            E parent = node.getParent();
            if (parent != null) {
                TreeNode<E> parentNode = map.computeIfAbsent(parent, TreeNode::new);
                parentNode.children().add(treeNode);
            } else {
                roots.add(treeNode);
            }
        }
        return roots;
    }
}
