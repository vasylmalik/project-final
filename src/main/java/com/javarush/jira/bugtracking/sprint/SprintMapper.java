package com.javarush.jira.bugtracking.sprint;

import com.javarush.jira.bugtracking.sprint.to.SprintDTO;
import com.javarush.jira.common.BaseMapper;
import com.javarush.jira.common.TimestampMapper;
import com.javarush.jira.common.error.DataConflictException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = TimestampMapper.class)
public interface SprintMapper extends BaseMapper<Sprint, SprintDTO> {
    static long checkProjectBelong(long projectId, Sprint dbSprint) {
        if (projectId != dbSprint.getProjectId())
            throw new DataConflictException("Sprint " + dbSprint.id() + " doesn't belong to Project " + projectId);
        return projectId;
    }

    @Override
    @Mapping(target = "projectId", expression = "java(SprintMapper.checkProjectBelong(sprintDTO.getProjectId(), sprint))")
    Sprint updateFromDTO(SprintDTO sprintDTO, @MappingTarget Sprint sprint);
}
