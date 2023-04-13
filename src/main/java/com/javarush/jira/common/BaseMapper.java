package com.javarush.jira.common;

import com.javarush.jira.common.model.BaseEntity;
import com.javarush.jira.common.to.BaseTo;
import org.mapstruct.MappingTarget;

import java.util.Collection;
import java.util.List;

public interface BaseMapper<E extends BaseEntity, T extends BaseTo> {

    E toEntity(T to);

    List<E> toEntityList(Collection<T> tos);

    E updateFromTo(@MappingTarget E entity, T to);

    T toTo(E entity);

    List<T> toToList(Collection<E> entities);
}
