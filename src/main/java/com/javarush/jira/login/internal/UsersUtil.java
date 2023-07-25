package com.javarush.jira.login.internal;

import static com.javarush.jira.common.internal.config.SecurityConfig.PASSWORD_ENCODER;
import com.javarush.jira.login.User;

public class UsersUtil {

    public static User prepareForCreate(User user) {
        return prepareForUpdate(user, PASSWORD_ENCODER.encode(user.getPassword()));
    }

    public static User prepareForUpdate(User user, String encPassword) {
        user.setPassword(encPassword);
        user.normalize();
        return user;
    }
}
