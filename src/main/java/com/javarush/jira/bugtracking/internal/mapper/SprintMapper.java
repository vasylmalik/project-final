package com.javarush.jira.bugtracking.internal.mapper;

import com.javarush.jira.bugtracking.internal.model.Sprint;
import com.javarush.jira.bugtracking.to.SprintTo;
import com.javarush.jira.common.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface SprintMapper extends BaseMapper<Sprint, SprintTo> {

    @Mapping(target = "enabled", expression = "java(sprint.isEnabled())")
    @Override
    SprintTo toTo(Sprint sprint);

    @Override
    Sprint toEntity(SprintTo sprintTo);

    @Override
    List<SprintTo> toToList(Collection<Sprint> sprints);
}
