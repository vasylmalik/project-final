package com.javarush.jira.bugtracking.sprint;

import com.javarush.jira.bugtracking.sprint.to.SprintToFull;
import com.javarush.jira.common.BaseMapper;
import com.javarush.jira.common.TimestampMapper;
import org.mapstruct.Mapper;

@Mapper(config = TimestampMapper.class)
public interface SprintMapperFull extends BaseMapper<Sprint, SprintToFull> {
}
