package com.javarush.jira.profile.web;

import com.javarush.jira.MatcherFactory;
import com.javarush.jira.common.util.JsonUtil;
import com.javarush.jira.profile.ContactTo;
import com.javarush.jira.profile.ProfileTo;

import java.util.Set;

// TODO: 5. add tests for ProfileRestController
public class ProfileTestData {

    public static final MatcherFactory.Matcher<ProfileTo> TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(ProfileTo.class, "lastLogin");

    public static final long USER_ID = 1L;
    public static final long ADMIN_ID = 2L;

    public static final String USER_MAIL = "user@gmail.com";
    public static final String ADMIN_MAIL = "admin@gmail.com";
    public static final String USER_PASSWORD = "password";

    public static final String ASSIGNED = "assigned";
    public static final String THREE_DAYS_BEFORE_DEADLINE = "three_days_before_deadline";
    public static final String TWO_DAYS_BEFORE_DEADLINE = "two_days_before_deadline";
    public static final String ONE_DAY_BEFORE_DEADLINE = "one_day_before_deadline";
    public static final String DEADLINE = "deadline";
    public static final String OVERDUE = "overdue";

    public static final Set<String> USER_MAIL_NOTIFICATIONS = Set.of(ASSIGNED, DEADLINE, OVERDUE);
    public static final Set<String> ADMIN_MAIL_NOTIFICATIONS = Set.of(ONE_DAY_BEFORE_DEADLINE, TWO_DAYS_BEFORE_DEADLINE, THREE_DAYS_BEFORE_DEADLINE);

    public static final ContactTo CONTACT_SKYPE = new ContactTo("skype", "userSkype");
    public static final ContactTo CONTACT_MOBILE = new ContactTo("mobile", "+01234567890");
    public static final ContactTo CONTACT_WEBSITE = new ContactTo("website", "user.com");
    public static final ContactTo CONTACT_GITHUB = new ContactTo("github", "adminGitHub");
    public static final ContactTo CONTACT_TG = new ContactTo("tg", "adminTg");

    public static final ContactTo USER_CONTACT_SKYPE = new ContactTo("skype", "userSkype");
    public static final ContactTo USER_CONTACT_WEBSITE = new ContactTo("website", "user.com");

    static {
        USER_CONTACT_SKYPE.setId(USER_ID);
        USER_CONTACT_WEBSITE.setId(USER_ID);
    }

    public static final Set<ContactTo> USER_CONTACTS = Set.of(CONTACT_SKYPE, CONTACT_MOBILE, CONTACT_WEBSITE);
    public static final Set<ContactTo> ADMIN_CONTACTS = Set.of(CONTACT_GITHUB, CONTACT_TG);

    public static final ProfileTo userProfileTo = new ProfileTo(USER_ID, USER_MAIL_NOTIFICATIONS, USER_CONTACTS);
    public static final ProfileTo adminProfileTo = new ProfileTo(ADMIN_ID, ADMIN_MAIL_NOTIFICATIONS, ADMIN_CONTACTS);

    public static ProfileTo getUpdated() {
        return new ProfileTo(USER_ID, Set.of(ONE_DAY_BEFORE_DEADLINE, DEADLINE), Set.of(USER_CONTACT_SKYPE, USER_CONTACT_WEBSITE));
    }

    public static <T> String jsonWithPassword(T profileTo, String password) {
        return JsonUtil.writeAdditionProps(profileTo, "password", password);
    }
}
