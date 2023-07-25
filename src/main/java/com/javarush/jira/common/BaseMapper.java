package com.javarush.jira.common;

import com.javarush.jira.common.to.BaseTo;
import java.util.Collection;
import java.util.List;
import org.mapstruct.MappingTarget;

public interface BaseMapper<E, T extends BaseTo> {

    E toEntity(T to);

    List<E> toEntityList(Collection<T> tos);

    E updateFromTo(T to, @MappingTarget E entity);

    T toTo(E entity);

    List<T> toToList(Collection<E> entities);
}
