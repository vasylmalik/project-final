package com.javarush.jira.profile.internal;

import com.javarush.jira.profile.ContactDTO;
import com.javarush.jira.ref.RefDTO;
import com.javarush.jira.ref.RefType;
import com.javarush.jira.ref.ReferenceService;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class ProfileUtil {
    public static Set<String> maskToNotifications(long notifications) {
        Set<String> notificationsCodes = ReferenceService.getRefs(RefType.MAIL_NOTIFICATION).values().stream()
                .filter(ref -> (notifications & ref.getLongFromAux()) != 0)
                .map(RefDTO::getCode)
                .collect(Collectors.toSet());
        return notificationsCodes.isEmpty() ? Set.of() : notificationsCodes;
    }

    public static long notificationsToMask(Set<String> notifications) {
        return notifications.stream()
                .map(code -> ReferenceService.getRefDTO(RefType.MAIL_NOTIFICATION, code))
                .map(RefDTO::getLongFromAux)
                .reduce(0L, (mask1, mask2) -> mask1 | mask2);
    }

    public static void checkContactsExist(Collection<ContactDTO> contacts) {
        contacts.forEach(c -> ReferenceService.getRefDTO(RefType.CONTACT, c.getCode()));
    }
}
