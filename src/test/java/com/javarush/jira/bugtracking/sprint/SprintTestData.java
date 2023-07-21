package com.javarush.jira.bugtracking.sprint;

import com.javarush.jira.MatcherFactory;
import com.javarush.jira.bugtracking.sprint.to.SprintTo;

public class SprintTestData {
    public static final MatcherFactory.Matcher<SprintTo> SPRINT_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(
            SprintTo.class, "enabled");
    public static final MatcherFactory.Matcher<Sprint> SPRINT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(
            Sprint.class, "startpoint", "endpoint", "project");

    public static final long SPRINT1_ID = 1;
    public static final long PROJECT1_ID = 1;
    public static final long NOT_FOUND = 100;
    public static final String ACTIVE = "active";

    public static final SprintTo sprintTo1 = new SprintTo(SPRINT1_ID, "SP-1.001", "finished", PROJECT1_ID);
    public static final SprintTo sprintTo2 = new SprintTo(SPRINT1_ID + 1, "SP-1.002", "active", PROJECT1_ID);
    public static final SprintTo sprintTo3 = new SprintTo(SPRINT1_ID + 2, "SP-1.003", "active", PROJECT1_ID);
    public static final SprintTo sprintTo4 = new SprintTo(SPRINT1_ID + 3, "SP-1.004", "planning", PROJECT1_ID);

    static {
        sprintTo1.setEnabled(false);
    }

    public static SprintTo getNewTo() {
        return new SprintTo(null, "SP.1-005", "planning", PROJECT1_ID);
    }

    public static SprintTo getUpdatedTo() {
        return new SprintTo(SPRINT1_ID, "SP-1.001 updated", "active", PROJECT1_ID);
    }
}
