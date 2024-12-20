package com.javarush.jira.profile.internal.web;

import com.javarush.jira.MatcherFactory;
import com.javarush.jira.profile.ContactDTO;
import com.javarush.jira.profile.ProfileDTO;

import java.util.Collections;
import java.util.Set;

public class ProfileTestData {

    public static MatcherFactory.Matcher<ProfileDTO> PROFILE_DTO_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(ProfileDTO.class, "user");

    public static final Long ADMIN_ID = 2L;
    public static final Long USER_ID = 1L;

    private static final String ASSIGNED = "assigned";
    private static final String THREE_DAYS_BEFORE_DEADLINE = "three_days_before_deadline";
    private static final String TWO_DAYS_BEFORE_DEADLINE = "two_days_before_deadline";
    private static final String ONE_DAY_BEFORE_DEADLINE = "one_day_before_deadline";
    private static final String DEADLINE = "deadline";
    private static final String OVERDUE = "overdue";

    private static final ContactDTO USER_CONTACT_SKYPE = new ContactDTO("skype", "userSkype");
    private static final ContactDTO USER_CONTACT_MOBILE = new ContactDTO("mobile", "+01234567890");
    private static final ContactDTO USER_CONTACT_WEBSITE = new ContactDTO("website", "user.com");

    private static final ContactDTO USER_CONTACT_SKYPE_UPDATED = new ContactDTO("skype", "newSkype");
    private static final ContactDTO USER_CONTACT_MOBILE_UPDATED = new ContactDTO("mobile", "+380987654321");
    private static final ContactDTO USER_CONTACT_WEBSITE_UPDATED = new ContactDTO("website", "new.com");
    private static final ContactDTO USER_CONTACT_GITHUB_UPDATED = new ContactDTO("github", "newGitHub");
    private static final ContactDTO USER_CONTACT_TG_UPDATED = new ContactDTO("tg", "newTg");
    private static final ContactDTO USER_CONTACT_VK_UPDATED = new ContactDTO("vk", "newVk");
    private static final ContactDTO USER_CONTACT_LINKEDIN_UPDATED = new ContactDTO("linkedin", "newLinkedin");

    private static final ContactDTO ADMIN_CONTACT_GITHUB = new ContactDTO("github", "adminGitHub");
    private static final ContactDTO ADMIN_CONTACT_TG = new ContactDTO("tg", "adminTg");
    private static final ContactDTO ADMIN_CONTACT_VK = new ContactDTO("vk", "adminVk");

    private static final ContactDTO ADMIN_CONTACT_GITHUB_UPDATED = new ContactDTO("github", "newAdminGitHub");
    private static final ContactDTO ADMIN_CONTACT_TG_UPDATED = new ContactDTO("tg", "newAdminTg");
    private static final ContactDTO ADMIN_CONTACT_VK_UPDATED = new ContactDTO("vk", "newAdminVk");
    private static final ContactDTO ADMIN_CONTACT_SKYPE_UPDATED = new ContactDTO("skype", "newAdminSkype");
    private static final ContactDTO ADMIN_CONTACT_MOBILE_UPDATED = new ContactDTO("mobile", "newAdminMobile");
    private static final ContactDTO ADMIN_CONTACT_WEBSITE_UPDATED = new ContactDTO("website", "newAdminWebsite");
    private static final ContactDTO ADMIN_CONTACT_LINKEDIN_UPDATED = new ContactDTO("linkedin", "newAdminLinkedin");

    public static final Set<String> USER_NOTIFICATIONS = Set.of(
            ASSIGNED,
            OVERDUE,
            DEADLINE
    );

    public static final Set<ContactDTO> USER_CONTACTS = Set.of(
            USER_CONTACT_SKYPE,
            USER_CONTACT_MOBILE,
            USER_CONTACT_WEBSITE
    );

    public static final Set<String> USER_NOTIFICATIONS_UPDATED = Set.of(
            ASSIGNED,
            THREE_DAYS_BEFORE_DEADLINE,
            TWO_DAYS_BEFORE_DEADLINE,
            ONE_DAY_BEFORE_DEADLINE,
            DEADLINE,
            OVERDUE
    );

    public static final Set<ContactDTO> USER_CONTACTS_UPDATED = Set.of(
            USER_CONTACT_SKYPE_UPDATED,
            USER_CONTACT_MOBILE_UPDATED,
            USER_CONTACT_WEBSITE_UPDATED,
            USER_CONTACT_GITHUB_UPDATED,
            USER_CONTACT_TG_UPDATED,
            USER_CONTACT_VK_UPDATED,
            USER_CONTACT_LINKEDIN_UPDATED
    );

    public static final Set<String> ADMIN_NOTIFICATIONS = Set.of(
            TWO_DAYS_BEFORE_DEADLINE,
            ONE_DAY_BEFORE_DEADLINE,
            THREE_DAYS_BEFORE_DEADLINE
    );

    public static final Set<ContactDTO> ADMIN_CONTACTS = Set.of(
            ADMIN_CONTACT_GITHUB,
            ADMIN_CONTACT_TG,
            ADMIN_CONTACT_VK
    );

    public static final Set<String> ADMIN_NOTIFICATIONS_UPDATED = Set.of(
            THREE_DAYS_BEFORE_DEADLINE,
            TWO_DAYS_BEFORE_DEADLINE,
            ONE_DAY_BEFORE_DEADLINE,
            DEADLINE
    );

    public static final Set<ContactDTO> ADMIN_CONTACTS_UPDATED = Set.of(
            ADMIN_CONTACT_GITHUB_UPDATED,
            ADMIN_CONTACT_TG_UPDATED,
            ADMIN_CONTACT_VK_UPDATED,
            ADMIN_CONTACT_SKYPE_UPDATED,
            ADMIN_CONTACT_MOBILE_UPDATED,
            ADMIN_CONTACT_WEBSITE_UPDATED,
            ADMIN_CONTACT_LINKEDIN_UPDATED
    );

    public static ProfileDTO ADMIN_PROFILE_DTO = new ProfileDTO(
            ADMIN_ID,
            ADMIN_NOTIFICATIONS,
            ADMIN_CONTACTS
    );

    public static ProfileDTO USER_PROFILE_DTO = new ProfileDTO(
            USER_ID,
            USER_NOTIFICATIONS,
            USER_CONTACTS
    );

    public static ProfileDTO getUpdatedUserDTO() {
        return new ProfileDTO(
                USER_ID,
                USER_NOTIFICATIONS_UPDATED,
                USER_CONTACTS_UPDATED
        );
    }

    public static ProfileDTO getUpdatedAdminDTO() {
        return new ProfileDTO(
                ADMIN_ID,
                ADMIN_NOTIFICATIONS_UPDATED,
                ADMIN_CONTACTS_UPDATED
        );
    }

    public static ProfileDTO getInvalidDTO() {
        return new ProfileDTO(null,
                Set.of(""),
                Set.of(new ContactDTO("skype", "")));
    }

    public static ProfileDTO getWithUnknownNotificationDTO() {
        return new ProfileDTO(null,
                Set.of("WrongNotification"),
                Collections.emptySet());
    }

    public static ProfileDTO getWithUnknownContactDTO() {
        return new ProfileDTO(null,
                Collections.emptySet(),
                Set.of(new ContactDTO("WrongContactCode", "contact")));
    }

    public static ProfileDTO getWithContactHtmlUnsafeDTO() {
        return new ProfileDTO(null,
                Collections.emptySet(),
                Set.of(new ContactDTO("tg", "<script>alert(123)</script>")));
    }
}