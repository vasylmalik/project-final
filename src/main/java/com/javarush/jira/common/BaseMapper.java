package com.javarush.jira.common;

import com.javarush.jira.common.to.BaseDTO;
import org.mapstruct.MappingTarget;

import java.util.Collection;
import java.util.List;

public interface BaseMapper<E, T extends BaseDTO> {

    E toEntity(T to);

    List<E> toEntityList(Collection<T> dtos);

    E updateFromDTO(T to, @MappingTarget E entity);

    T toDTO(E entity);

    List<T> toDTOList(Collection<E> entities);
}
