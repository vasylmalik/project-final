package com.javarush.jira.common;

public interface HasIdAndParentId extends HasId {
    Long getParentId();
}
