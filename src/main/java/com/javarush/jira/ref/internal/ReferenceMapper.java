package com.javarush.jira.ref.internal;

import com.javarush.jira.ref.RefTo;
import com.javarush.jira.ref.RefType;
import org.springframework.stereotype.Component;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReferenceMapper {

    // 1. Конвертація з бази в DTO (RefTo)
    public RefTo toTo(Reference entity) {
        if (entity == null) return null;

        RefTo to = new RefTo(
                entity.getId(),
                entity.getRefType(),
                entity.getCode(),
                entity.getTitle(),
                entity.getAux()
        );
        to.setEnabled(entity.isEnabled());

        // Примусово записуємо в приховане поле 'code' через рефлексію
        setHiddenField(to, "code", entity.getCode());
        return to;
    }

    // 2. Конвертація колекцій для ReferenceService
    public List<RefTo> toToList(Collection<Reference> references) {
        if (references == null) return null;
        return references.stream().map(this::toTo).collect(Collectors.toList());
    }

    public Reference toEntity(RefTo to) {
        if (to == null) return null;

        Reference entity = new Reference();
        entity.setId(to.getId());
        entity.setEnabled(to.isEnabled());
        entity.setAux(to.getAux());

        // Прямо викликаємо геттери, успадковані від TitleTo
        entity.setRefType(to.getRefType());
        entity.setCode(to.getCode());
        entity.setTitle(to.getTitle());

        // Якщо геттери повернули null, робимо примусовий фолбек на рефлексію
        if (entity.getCode() == null) {
            entity.setCode((String) getHiddenField(to, "code"));
        }
        if (entity.getTitle() == null) {
            entity.setTitle((String) getHiddenField(to, "title"));
        }
        if (entity.getRefType() == null) {
            entity.setRefType((RefType) getHiddenField(to, "refType"));
        }

        return entity;
    }

    // 🛠 БЕЗПЕЧНИЙ ЗАПИС: пробиває будь-яку архітектурну ієрархію Spring
    private void setHiddenField(Object target, String fieldName, Object value) {
        Class<?> current = target.getClass();
        while (current != Object.class) {
            if (current.getName().contains("$$") || current.getName().contains("CGLIB")) {
                current = current.getSuperclass();
                continue;
            }
            try {
                Field field = current.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(target, value);
                return;
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            } catch (Exception ignored) {
                return;
            }
        }
    }

    // 🛠 БЕЗПЕЧНЕ ЗЧИТУВАННЯ: дістає дані, навіть якщо поля приватні/package-private
    private Object getHiddenField(Object target, String fieldName) {
        Class<?> current = target.getClass();
        while (current != Object.class) {
            if (current.getName().contains("$$") || current.getName().contains("CGLIB")) {
                current = current.getSuperclass();
                continue;
            }
            try {
                Field field = current.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(target);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            } catch (Exception ignored) {
                return null;
            }
        }
        return null;
    }
}