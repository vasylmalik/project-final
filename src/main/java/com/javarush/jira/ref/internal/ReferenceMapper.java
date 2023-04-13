package com.javarush.jira.ref.internal;

import com.javarush.jira.common.BaseMapper;
import com.javarush.jira.ref.RefTo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ReferenceMapper extends BaseMapper<Reference, RefTo> {

    @Mapping(target = "enabled", expression = "java(reference.isEnabled())")
    @Override
    RefTo toTo(Reference reference);

    @Override
    Reference toEntity(RefTo refTo);

    @Override
    List<RefTo> toToList(Collection<Reference> references);
}
