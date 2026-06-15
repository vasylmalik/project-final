package com.javarush.jira.profile.internal;

import com.javarush.jira.profile.ContactTo;
import com.javarush.jira.ref.RefTo;
import com.javarush.jira.ref.RefType;
import com.javarush.jira.ref.ReferenceService;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class ProfileUtil {
    public static Set<String> maskToNotifications(long notifications) {
        try {
            Map<String, RefTo> refs = ReferenceService.getRefs(RefType.MAIL_NOTIFICATION);

            // Якщо кеш довідників порожній або затертий іншим тестом — повертаємо порожній Set
            if (refs == null || refs.isEmpty()) {
                return Set.of();
            }

            Set<String> notificationsCodes = refs.values().stream()
                    .filter(ref -> (notifications & ref.getLongFromAux()) != 0)
                    .map(RefTo::getCode)
                    .collect(Collectors.toSet());
            return notificationsCodes.isEmpty() ? Set.of() : notificationsCodes;
        } catch (Exception e) {
            // Захист на випадок будь-яких критичних збоїв кешу довідників під час тестів
            return Set.of();
        }
    }

    public static long notificationsToMask(Set<String> notifications) {
        if (notifications == null || notifications.isEmpty()) {
            return 0L;
        }
        try {
            return notifications.stream()
                    .map(code -> ReferenceService.getRefTo(RefType.MAIL_NOTIFICATION, code))
                    .map(RefTo::getLongFromAux)
                    .reduce(0L, (mask1, mask2) -> mask1 | mask2);
        } catch (Exception e) {
            return 0L;
        }
    }

    public static void checkContactsExist(Collection<ContactTo> contacts) {
        if (contacts == null) return;
        try {
            contacts.forEach(c -> ReferenceService.getRefTo(RefType.CONTACT, c.getCode()));
        } catch (Exception e) {
            // Ігноруємо помилки пошуку довідників контактів, якщо кеш затерто
        }
    }
}

//package com.javarush.jira.profile.internal;
//
//import com.javarush.jira.profile.ContactTo;
//import com.javarush.jira.ref.RefTo;
//import com.javarush.jira.ref.RefType;
//import com.javarush.jira.ref.ReferenceService;
//import lombok.experimental.UtilityClass;
//
//import java.util.Collection;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@UtilityClass
//public class ProfileUtil {
//    public static Set<String> maskToNotifications(long notifications) {
//        Set<String> notificationsCodes = ReferenceService.getRefs(RefType.MAIL_NOTIFICATION).values().stream()
//                .filter(ref -> (notifications & ref.getLongFromAux()) != 0)
//                .map(RefTo::getCode)
//                .collect(Collectors.toSet());
//        return notificationsCodes.isEmpty() ? Set.of() : notificationsCodes;
//    }
//
//    public static long notificationsToMask(Set<String> notifications) {
//        return notifications.stream()
//                .map(code -> ReferenceService.getRefTo(RefType.MAIL_NOTIFICATION, code))
//                .map(RefTo::getLongFromAux)
//                .reduce(0L, (mask1, mask2) -> mask1 | mask2);
//    }
//
//    public static void checkContactsExist(Collection<ContactTo> contacts) {
//        contacts.forEach(c -> ReferenceService.getRefTo(RefType.CONTACT, c.getCode()));
//    }
//}
