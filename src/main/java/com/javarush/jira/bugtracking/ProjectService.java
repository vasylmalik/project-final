package com.javarush.jira.bugtracking;

import com.javarush.jira.bugtracking.internal.mapper.ProjectMapper;
import com.javarush.jira.bugtracking.internal.model.Project;
import com.javarush.jira.bugtracking.internal.repository.ProjectRepository;
import com.javarush.jira.bugtracking.to.ProjectTo;
import org.springframework.stereotype.Service;

@Service
public class ProjectService extends BugtrackingService<Project, ProjectTo, ProjectRepository> {
    public ProjectService(ProjectRepository repository, ProjectMapper mapper) {
        super(repository, mapper);
    }
}
