package com.javarush.jira.project.internal.web;

import com.javarush.jira.MatcherFactory;
import com.javarush.jira.bugtracking.project.Project;
import com.javarush.jira.bugtracking.project.to.ProjectDTO;

public class ProjectTestData {
    public static final MatcherFactory.Matcher<Project> PROJECT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Project.class, "id", "startpoint", "endpoint");
    public static final MatcherFactory.Matcher<ProjectDTO> PROJECT_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(ProjectDTO.class, "id");
    public static final Long PARENT_PROJECT_ID = 1L;
    public static final Long PROJECT_ID = 2L;
    public static final ProjectDTO PROJECT_DTO_1 = new ProjectDTO(PARENT_PROJECT_ID, "PR1", "PROJECT-1", "test project 1", "task_tracker", null);

    public static final ProjectDTO PROJECT_DTO_2 = new ProjectDTO(PROJECT_ID, "PR2", "PROJECT-2", "test project 2", "task_tracker", PARENT_PROJECT_ID);

    public static Project getNew() {
        return new Project(null, "PR3", "PROJECT-3", "task_tracker", "test project 3", null);
    }

    public static ProjectDTO getUpdated() {
        return new ProjectDTO(PROJECT_ID, "PR2", "PROJECT-2 UPD", "test project 2", "task_tracker", PARENT_PROJECT_ID);
    }

    public static ProjectDTO getDisabled() {
        ProjectDTO projectDTO = new ProjectDTO(PROJECT_ID, "PR2", "PROJECT-2 UPD", "test project 2", "task_tracker", PARENT_PROJECT_ID);
        projectDTO.setEnabled(false);
        return projectDTO;
    }
}

