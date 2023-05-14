package com.javarush.jira.profile.web;

import com.javarush.jira.MatcherFactory;
import com.javarush.jira.common.util.JsonUtil;
import com.javarush.jira.login.Role;
import com.javarush.jira.login.User;
import com.javarush.jira.login.UserTo;
import com.javarush.jira.profile.ContactTo;
import com.javarush.jira.profile.ProfileTo;
import com.javarush.jira.profile.internal.Contact;
import com.javarush.jira.profile.internal.Profile;

import java.util.HashSet;
import java.util.Set;

public class ProfileTestData {
    public static final MatcherFactory.Matcher<ProfileTo> PROFILE_TO_MATCHER = MatcherFactory.usingEqualsComparator(
            ProfileTo.class);
    public static final MatcherFactory.Matcher<ProfileTo> PROFILE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(
            ProfileTo.class, "contacts");


    public static final MatcherFactory.Matcher<UserTo> TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(UserTo.class);

    public static final long USER_ID = 1L;

    public static final String USER_MAIL = "user@gmail.com";
    public static final String USER_PASSWORD = "password";

    public static final String ASSIGNED = "assigned";
    public static final String THREE_DAYS_BEFORE_DEADLINE = "three_days_before_deadline";
    public static final String TWO_DAYS_BEFORE_DEADLINE = "two_days_before_deadline";
    public static final String ONE_DAY_BEFORE_DEADLINE = "one_day_before_deadline";
    public static final String DEADLINE = "deadline";
    public static final String OVERDUE = "overdue";

    public static final Set<String> USER_MAIL_NOTIFICATIONS = Set.of(ASSIGNED, DEADLINE, OVERDUE);

    public static final ContactTo CONTACT_SKYPE = new ContactTo("skype", "userSkype");
    public static final ContactTo CONTACT_MOBILE = new ContactTo("mobile", "+01234567890");
    public static final ContactTo CONTACT_WEBSITE = new ContactTo("website", "user.com");

    public static final ContactTo USER_CONTACT_SKYPE = new ContactTo("skype", "userSkype");
    public static final ContactTo USER_CONTACT_WEBSITE = new ContactTo("website", "user.com");

    static {
        USER_CONTACT_SKYPE.setId(USER_ID);
        USER_CONTACT_WEBSITE.setId(USER_ID);
    }

    public static final Set<ContactTo> USER_CONTACTS = Set.of(CONTACT_SKYPE, CONTACT_MOBILE, CONTACT_WEBSITE);

    public static final ProfileTo userProfileTo = new ProfileTo(USER_ID, USER_MAIL_NOTIFICATIONS, USER_CONTACTS);

    public static ProfileTo getUpdated() {
        return new ProfileTo(USER_ID, Set.of(ONE_DAY_BEFORE_DEADLINE, DEADLINE), Set.of(USER_CONTACT_SKYPE, USER_CONTACT_WEBSITE));
    }

    public static <T> String jsonWithPassword(T profileTo, String password) {
        return JsonUtil.writeAdditionProps(profileTo, "password", password);
    }
}
