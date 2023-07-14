package com.javarush.jira.login.internal.web;

import com.javarush.jira.MatcherFactory;
import com.javarush.jira.common.util.JsonUtil;
import com.javarush.jira.login.Role;
import com.javarush.jira.login.User;
import com.javarush.jira.login.UserTo;

public class UserTestData {
    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(
            User.class, "startpoint", "endpoint", "password");

    public static final MatcherFactory.Matcher<UserTo> TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(UserTo.class);

    public static final long USER_ID = 1;
    public static final long ADMIN_ID = 2;
    public static final long GUEST_ID = 3;
    public static final long MANAGER_ID = 4;
    public static final long NOT_FOUND = 100;
    public static final String USER_MAIL = "user@gmail.com";
    public static final String ADMIN_MAIL = "admin@gmail.com";
    public static final String GUEST_MAIL = "guest@gmail.com";
    public static final String MANAGER_MAIL = "manager@gmail.com";

    public static final User user = new User(USER_ID, USER_MAIL, "password", "userFirstName", "userLastName",
            "userDisplayName", Role.DEV);
    public static final User admin = new User(ADMIN_ID, ADMIN_MAIL, "admin", "adminFirstName", "adminLastName",
            "adminDisplayName", Role.ADMIN, Role.DEV);
    public static final User guest = new User(GUEST_ID, GUEST_MAIL, "guest", "guestFirstName", "guestLastName",
            "guestDisplayName");
    public static final User manager = new User(MANAGER_ID, MANAGER_MAIL, "manager", "managerFirstName", "managerLastName", "managerDisplayName", Role.MANAGER);

    public static User getNew() {
        return new User(null, "new@gmail.com", "newPassword", "newFirstName", "newLastName", "newDisplayName", Role.DEV);
    }

    public static User getUpdated() {
        return new User(USER_ID, USER_MAIL, "updatedPassword", "updatedFirstName", "updatedLastName",
                "updatedDisplayName", Role.DEV, Role.ADMIN);
    }

    public static <T> String jsonWithPassword(T user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }
}
