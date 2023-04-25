package com.javarush.jira.profile.web;
import com.javarush.jira.MatcherFactory;
import com.javarush.jira.common.util.JsonUtil;
import com.javarush.jira.profile.ContactTo;
import com.javarush.jira.profile.ProfileTo;

import java.util.Set;

public interface ProfileTestData {
    MatcherFactory.Matcher<ProfileTo> PROFILE_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(ProfileTo.class, "lastLogin");

    long USER_ID = 1L;
    String USER_MAIL = "user@gmail.com";
    String USER_PASSWORD = "password";

    long ADMIN_ID = 2L;
    String ADMIN_MAIL = "admin@gmail.com";

    String ASSIGNED = "assigned";
    String DEADLINE = "deadline";
    String OVERDUE = "overdue";
    String THREE_DAYS_BEFORE_DEADLINE = "three_days_before_deadline";
    String TWO_DAYS_BEFORE_DEADLINE = "two_days_before_deadline";
    String ONE_DAY_BEFORE_DEADLINE = "one_day_before_deadline";

    ContactTo CONTACT_SKYPE = new ContactTo("skype", "userSkype");
    ContactTo CONTACT_WEBSITE = new ContactTo("website", "user.com");
    ContactTo CONTACT_MOBILE = new ContactTo("mobile", "+01234567890");
    ContactTo CONTACT_GITHUB = new ContactTo("github", "adminGitHub");
    ContactTo CONTACT_TG = new ContactTo("tg", "adminTg");

    ContactTo USER_CONTACT_SKYPE = getUserContactSkype();

    Set<ContactTo> USER_CONTACTS = Set.of(CONTACT_SKYPE, CONTACT_MOBILE, CONTACT_WEBSITE);
    Set<ContactTo> UPDATED_USER_CONTACTS = Set.of(USER_CONTACT_SKYPE);
    Set<String> USER_MAIL_NOTIFICATIONS = Set.of(ASSIGNED, DEADLINE, OVERDUE);
    Set<String> UPDATED_USER_MAIL_NOTIFICATIONS = Set.of(ONE_DAY_BEFORE_DEADLINE, DEADLINE);

    Set<ContactTo> ADMIN_CONTACTS = Set.of(CONTACT_GITHUB, CONTACT_TG);
    Set<String> ADMIN_MAIL_NOTIFICATIONS = Set.of(ONE_DAY_BEFORE_DEADLINE, TWO_DAYS_BEFORE_DEADLINE, THREE_DAYS_BEFORE_DEADLINE);

    ProfileTo USER_PROFILE_TO = new ProfileTo(USER_ID, USER_MAIL_NOTIFICATIONS, USER_CONTACTS);
    ProfileTo ADMIN_PROFILE_TO = new ProfileTo(ADMIN_ID, ADMIN_MAIL_NOTIFICATIONS, ADMIN_CONTACTS);

    static ProfileTo getInvalidProfileToUpdate() {
        return new ProfileTo(ADMIN_ID, Set.of(ONE_DAY_BEFORE_DEADLINE, DEADLINE), Set.of(getUserContactSkype()));
    }

    static ContactTo getUserContactSkype(){
        ContactTo userContactSkype = new ContactTo("skype", "userSkype");
        userContactSkype.setId(USER_ID);
        return userContactSkype;
    }

    static <T> String jsonWithPassword(T profileTo, String password) {
        return JsonUtil.writeAdditionProps(profileTo, "password", password);
    }
}