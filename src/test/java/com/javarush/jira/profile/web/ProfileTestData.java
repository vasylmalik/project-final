package com.javarush.jira.profile.web;

import com.javarush.jira.MatcherFactory;
import com.javarush.jira.common.util.JsonUtil;
import com.javarush.jira.profile.ProfileTo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProfileTestData {
    public static final MatcherFactory.Matcher<ProfileTo> TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(
            ProfileTo.class, "mailNotifications", "contacts"
    );

    public static final long PROFILE_ID = 1;
    public static final HashSet<String> MAIL_NOTIFICATIONS = new HashSet<>(List.of("assigned"));
    public static final HashSet<String> UPDATED_MAIL_NOTIFICATIONS = new HashSet<>(List.of("deadline", "overdue"));
    public static final ProfileTo profileTo = new ProfileTo(PROFILE_ID, MAIL_NOTIFICATIONS, null);
    public static final ProfileTo profileToForUpdate = new ProfileTo(PROFILE_ID, null, null);

    public static <T> String jsonWithMailNotifications(T profileTo, Set<String> mailNotifications) {
        return JsonUtil.writeAdditionProps(profileTo, "mail_notifications", mailNotifications);
    }
}
