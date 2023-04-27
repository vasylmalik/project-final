package com.javarush.jira.bugtracking;

import com.javarush.jira.common.util.JsonUtil;

public class TaskTestData {
    public static final String USER_MAIL = "user@gmail.com";
    public static final String SUMMARY_URL = "/tasks/22/summary";
    public static final String EMPTY_SUMMARY_URL = "/tasks/23/summary";
    public static final String NO_SUMMARY = "Task is still in progress or has not started yet";

    public static <T> String jsonWithPassword(T user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }
}
