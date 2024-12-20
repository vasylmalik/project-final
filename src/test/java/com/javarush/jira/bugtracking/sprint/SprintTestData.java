package com.javarush.jira.bugtracking.sprint;

import com.javarush.jira.MatcherFactory;
import com.javarush.jira.bugtracking.sprint.to.SprintDTO;

public class SprintTestData {
    public static final MatcherFactory.Matcher<SprintDTO> SPRINT_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(
            SprintDTO.class, "enabled");
    public static final MatcherFactory.Matcher<Sprint> SPRINT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(
            Sprint.class, "startpoint", "endpoint", "project");

    public static final long SPRINT1_ID = 1;
    public static final long PROJECT1_ID = 1;
    public static final long NOT_FOUND = 100;
    public static final String ACTIVE = "active";

    public static final SprintDTO SPRINT_DTO_1 = new SprintDTO(SPRINT1_ID, "SP-1.001", "finished", PROJECT1_ID);
    public static final SprintDTO SPRINT_DTO_2 = new SprintDTO(SPRINT1_ID + 1, "SP-1.002", "active", PROJECT1_ID);
    public static final SprintDTO SPRINT_DTO_3 = new SprintDTO(SPRINT1_ID + 2, "SP-1.003", "active", PROJECT1_ID);
    public static final SprintDTO SPRINT_DTO_4 = new SprintDTO(SPRINT1_ID + 3, "SP-1.004", "planning", PROJECT1_ID);

    static {
        SPRINT_DTO_1.setEnabled(false);
    }

    public static SprintDTO getNewTo() {
        return new SprintDTO(null, "SP.1-005", "planning", PROJECT1_ID);
    }

    public static SprintDTO getUpdatedTo() {
        return new SprintDTO(SPRINT1_ID, "SP-1.001 updated", "active", PROJECT1_ID);
    }
}
