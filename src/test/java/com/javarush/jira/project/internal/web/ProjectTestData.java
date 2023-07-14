package com.javarush.jira.project.internal.web;

import com.javarush.jira.MatcherFactory;
import com.javarush.jira.bugtracking.project.Project;
import com.javarush.jira.bugtracking.project.to.ProjectTo;

public class ProjectTestData {
    public static final MatcherFactory.Matcher<Project> PROJECT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Project.class, "id", "startpoint", "endpoint");
    public static final MatcherFactory.Matcher<ProjectTo> PROJECT_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(ProjectTo.class, "id");
    public static final Long PARENT_PROJECT_ID = 1L;
    public static final Long PROJECT_ID = 2L;
    public static final ProjectTo projectTo1 = new ProjectTo(PARENT_PROJECT_ID, "PR1", "PROJECT-1", "test project 1", "task_tracker", null);

    public static final ProjectTo projectTo2 = new ProjectTo(PROJECT_ID, "PR2", "PROJECT-2", "test project 2", "task_tracker", PARENT_PROJECT_ID);

    public static Project getNew() {
        return new Project(null, "PR3", "PROJECT-3", "task_tracker", "test project 3", null);
    }

    public static ProjectTo getUpdated() {
        return new ProjectTo(PROJECT_ID, "PR2", "PROJECT-2 UPD", "test project 2", "task_tracker", PARENT_PROJECT_ID);
    }

    public static ProjectTo getDisabled() {
        ProjectTo projectTo = new ProjectTo(PROJECT_ID, "PR2", "PROJECT-2 UPD", "test project 2", "task_tracker", PARENT_PROJECT_ID);
        projectTo.setEnabled(false);
        return projectTo;
    }
}

