package com.javarush.jira.bugtracking.internal.mapper;

import com.javarush.jira.bugtracking.internal.model.Project;
import com.javarush.jira.bugtracking.to.ProjectTo;
import com.javarush.jira.common.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper extends BaseMapper<Project, ProjectTo> {

    @Mapping(target = "enabled", expression = "java(project.isEnabled())")
    @Override
    ProjectTo toTo(Project project);

    @Override
    Project toEntity(ProjectTo projectTo);

    @Override
    List<ProjectTo> toToList(Collection<Project> projects);
}
