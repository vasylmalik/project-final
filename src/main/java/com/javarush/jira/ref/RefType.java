package com.javarush.jira.ref;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RefType {
    CONTACT,
    PROJECT,
    TASK,
    TASK_STATUS,
    SPRINT_STATUS,
    USER_TYPE,
    MAIL_NOTIFICATION,
    PRIORITY
}
