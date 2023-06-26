package com.javarush.jira.ref;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RefType {
    CONTACT,           // 0
    PROJECT,           // 1
    TASK,              // 2
    TASK_STATUS,       // 3
    SPRINT_STATUS,     // 4
    USER_TYPE,         // 5
    MAIL_NOTIFICATION, // 6
    PRIORITY           // 7
}
