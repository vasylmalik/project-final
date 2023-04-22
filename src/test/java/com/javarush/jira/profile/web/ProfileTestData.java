package com.javarush.jira.profile.web;

import com.javarush.jira.MatcherFactory;
import com.javarush.jira.common.util.JsonUtil;
import com.javarush.jira.login.Role;
import com.javarush.jira.login.User;
import com.javarush.jira.login.UserTo;
import com.javarush.jira.profile.ProfileTo;
import com.javarush.jira.profile.internal.Profile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ProfileTestData {
    public static final String USER_MAIL = "user@gmail.com";

    public static final MatcherFactory.Matcher<ProfileTo> PROFILE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(
            ProfileTo.class);

    public static final MatcherFactory.Matcher<ProfileTo> TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(ProfileTo.class);

    public static final ProfileTo profileTo = new ProfileTo(1L, new HashSet<String>(Arrays.asList("assigned","overdue","deadline")), null);

    public static final ProfileTo profileTo1 = new ProfileTo(1L, new HashSet<String>(Arrays.asList("assigned")), null);

    public static <T> String jsonWithMailNotifications(T profileTo, Set<String> mailNotifications) {
        return JsonUtil.writeAdditionProps(profileTo, "mailNotifications", mailNotifications);
    }





}
