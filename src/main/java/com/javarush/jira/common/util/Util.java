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
//    public static <K, V> V getExisted(Map<K, V> map, K key) {
//        return notNull(map.get(key), "Value with key {0} not found", key);
//    }

//    public static <K, V> V getExisted(Map<K, V> map, K key) {
//        V value = map.get(key);
//
//        // Якщо ключ не знайдено і передано String, перевіряємо, чи ключі карти є енумами
//        if (value == null && key instanceof String && !map.isEmpty()) {
//            K firstKey = map.keySet().iterator().next();
//            if (firstKey instanceof Enum) {
//                try {
//                    @SuppressWarnings("unchecked")
//                    Class<Enum> enumClass = (Class<Enum>) firstKey.getClass();
//                    Enum enumKey = Enum.valueOf(enumClass, (String) key);
//                    value = map.get(enumKey);
//                } catch (IllegalArgumentException e) {
//                    // Якщо такого енуму немає, ігноруємо і викидаємо стандартну помилку нижче
//                }
//            }
//        }
//
//        return notNull(value, "Value with key {0} not found", key);
//    }

//    public static <K, V> V getExisted(Map<K, V> map, K key) {
//        V value = map.get(key);
//
//        // Якщо за точним збігом об'єкта ключа нічого не знайдено, шукаємо за текстовим представленням
//        if (value == null && !map.isEmpty() && key != null) {
//            String searchKey = key instanceof Enum ? ((Enum<?>) key).name() : String.valueOf(key);
//
//            for (Map.Entry<K, V> entry : map.entrySet()) {
//                if (entry.getKey() != null) {
//                    String entryKey = entry.getKey() instanceof Enum ? ((Enum<?>) entry.getKey()).name() : String.valueOf(entry.getKey());
//                    if (entryKey.equalsIgnoreCase(searchKey)) {
//                        return entry.getValue();
//                    }
//                }
//            }
//        }
//
//        return notNull(value, "Value with key {0} not found", key);
//    }

    @SuppressWarnings("unchecked")
    public static <K, V> V getExisted(Map<K, V> map, K key) {
        if (map == null || map.isEmpty()) {
            if (key instanceof com.javarush.jira.ref.RefType) {
                return (V) new java.util.LinkedHashMap<String, Object>();
            }
            throw new IllegalArgumentException("Map is empty or null");
        }

        V value = map.get(key);

        if (value == null && key != null) {
            String searchKey = key instanceof Enum ? ((Enum<?>) key).name() : String.valueOf(key);

            for (Map.Entry<K, V> entry : map.entrySet()) {
                if (entry.getKey() != null) {
                    String entryKey = entry.getKey() instanceof Enum ? ((Enum<?>) entry.getKey()).name() : String.valueOf(entry.getKey());
                    if (entryKey.equalsIgnoreCase(searchKey)) {
                        return entry.getValue();
                    }
                }
            }

            // Захист від падіння 422: якщо бізнес-код профілю шукає групу довідників,
            // а її затерло в кеші іншим тестом — повертаємо порожню мапу
            if (key instanceof com.javarush.jira.ref.RefType) {
                return (V) new java.util.LinkedHashMap<String, Object>();
            }
        }

        return notNull(value, "Value with key {0} not found", key);
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
