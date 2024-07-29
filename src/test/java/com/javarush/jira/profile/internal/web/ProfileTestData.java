package com.javarush.jira.profile.internal.web;

import com.javarush.jira.MatcherFactory;
import com.javarush.jira.profile.ContactTo;
import com.javarush.jira.profile.ProfileTo;
import com.javarush.jira.profile.internal.ProfileUtil;
import com.javarush.jira.profile.internal.model.Profile;
import static com.javarush.jira.login.internal.web.UserTestData.*;

import java.util.Collections;
import java.util.Set;

public class ProfileTestData {
    public static MatcherFactory.Matcher<ProfileTo> PROFILE_TO_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(ProfileTo.class);

    public static final ProfileTo ADMIN_PROFILE_TO = new ProfileTo(
            ADMIN_ID,
            ProfileUtil.maskToNotifications(14),
            Set.of(
                    new ContactTo("github", "adminGitHub"),
                    new ContactTo("tg", "adminTg"),
                    new ContactTo("vk", "adminVk")
            ));

    public static ProfileTo USER_PROFILE_TO = new ProfileTo(
            USER_ID,
            Set.of("assigned", "overdue", "deadline"),
            Set.of(new ContactTo("skype", "userSkype"),
                    new ContactTo("mobile", "+01234567890"),
                    new ContactTo("website", "user.com")));
    public static ProfileTo GUEST_PROFILE_EMPTY_TO = new ProfileTo(
            GUEST_ID,
            Set.of(),
            Set.of());

    public static ProfileTo getUpdatedTo(Long id) {
        return new ProfileTo(id,
                Set.of("assigned", "three_days_before_deadline", "two_days_before_deadline", "one_day_before_deadline", "deadline", "overdue"),
                Set.of(new ContactTo("skype", "newSkype"),
                        new ContactTo("mobile", "+380987654321"),
                        new ContactTo("website", "new.com"),
                        new ContactTo("github", "newGitHub"),
                        new ContactTo("tg", "newTg"),
                        new ContactTo("linkedin", "newLinkedin")));
    }

    public static ProfileTo getInvalidTo(Long id) {
        return new ProfileTo(id,
                Set.of(""),
                Set.of(new ContactTo("skype", "")));
    }

    public static ProfileTo getWithUnknownNotificationTo(long id) {
        return new ProfileTo(id,
                Set.of("WrongNotification"),
                Collections.emptySet());
    }

    public static ProfileTo getWithUnknownContactTo(Long id) {
        return new ProfileTo(id,
                Collections.emptySet(),
                Set.of(new ContactTo("WrongContactCode", "contact")));
    }

    public static ProfileTo getWithContactHtmlUnsafeTo(Long id) {
        return new ProfileTo(id,
                Collections.emptySet(),
                Set.of(new ContactTo("tg", "<script>alert(123)</script>")));
    }
}
