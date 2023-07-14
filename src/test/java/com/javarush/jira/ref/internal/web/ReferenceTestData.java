package com.javarush.jira.ref.internal.web;

import com.javarush.jira.MatcherFactory;
import com.javarush.jira.ref.RefTo;
import com.javarush.jira.ref.RefType;
import com.javarush.jira.ref.internal.Reference;

public class ReferenceTestData {
    public static final MatcherFactory.Matcher<Reference> REFERENCE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Reference.class, "id", "startpoint", "endpoint");
    public static final MatcherFactory.Matcher<RefTo> REFTO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(RefTo.class, "id");
    public static final String TASK_CODE = "task";
    public static final Reference ref = new Reference(1L, RefType.TASK, TASK_CODE, "Task");
    public static final RefTo refTo = new RefTo(null, RefType.TASK, TASK_CODE, "Task", null);

    static {
        ref.setEnabled(false);
    }

    public static Reference getNew() {
        return new Reference(null, RefType.TASK, "enhancement", "Enhancement");
    }

    public static Reference getUpdated() {
        return new Reference(null, RefType.TASK, "task", "Task1");
    }

}
