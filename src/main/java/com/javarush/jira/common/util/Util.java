package com.javarush.jira.common.util;

import com.javarush.jira.bugtracking.tree.ITreeNode;
import com.javarush.jira.common.HasIdAndParentId;
import com.javarush.jira.common.error.NotFoundException;
import com.javarush.jira.common.model.TimestampEntry;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;
import org.jsoup.Jsoup;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

@UtilityClass
public class Util {
    public static <K, V> V getExisted(Map<K, V> map, K key) {
        return notNull(map.get(key), "Value with key {0} not found", key);
    }

    public static <T> T checkExist(long id, Optional<T> opt) {
        return opt.orElseThrow(() -> new NotFoundException("Entity with id=" + id + " not found"));
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

    public static <T extends HasIdAndParentId, R extends ITreeNode<T, R>> List<R> makeTree(List<T> nodes, Function<T, R> treeNodeCreator) {
        List<R> roots = new ArrayList<>();
        Map<Long, R> map = new HashMap<>();
        for (T node : nodes) {
            R treeNode = treeNodeCreator.apply(node);
            map.put(node.id(), treeNode);
            if (node.getParentId() == null) {
                roots.add(treeNode);
            }
        }
        for (T node : nodes) {
            if (node.getParentId() != null) {
                R parent = map.get(node.getParentId());
                R current = map.get(node.id());
                if (parent != null) {
                    parent.subNodes().add(current);
                } else {
                    roots.add(current);
                }
            }
        }
        return roots;
    }

    public boolean isEnabled(TimestampEntry entity) {
        return entity.getEndpoint() == null || entity.getEndpoint().isAfter(LocalDateTime.now());
    }
}
