package com.javarush.jira.bugtracking.project;

import com.javarush.jira.bugtracking.project.to.ProjectToFull;
import com.javarush.jira.common.BaseMapper;
import com.javarush.jira.common.TimestampMapper;
import org.mapstruct.Mapper;

@Mapper(config = TimestampMapper.class)
public interface ProjectMapperFull extends BaseMapper<Project, ProjectToFull> {
}
