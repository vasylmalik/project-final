package com.javarush.jira.bugtracking;

import com.javarush.jira.common.util.JsonUtil;

public class DashboardTestData {
    public static final String USER_MAIL = "user@gmail.com";

    public static <T> String jsonWithPassword(T user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }
}
