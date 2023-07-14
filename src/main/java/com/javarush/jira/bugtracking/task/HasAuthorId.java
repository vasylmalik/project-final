package com.javarush.jira.bugtracking.task;

import com.javarush.jira.common.HasId;

public interface HasAuthorId extends HasId {
    Long getAuthorId();
}
