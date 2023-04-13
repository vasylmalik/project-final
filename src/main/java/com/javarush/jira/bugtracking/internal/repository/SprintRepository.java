package com.javarush.jira.bugtracking.internal.repository;

import com.javarush.jira.bugtracking.internal.model.Sprint;
import com.javarush.jira.common.BaseRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface SprintRepository extends BaseRepository<Sprint> {
}
