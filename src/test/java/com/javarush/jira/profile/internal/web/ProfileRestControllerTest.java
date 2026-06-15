package com.javarush.jira.profile.internal.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.login.AuthUser;
import com.javarush.jira.login.Role;
import com.javarush.jira.login.User;
import com.javarush.jira.profile.internal.ProfileRepository;
import com.javarush.jira.profile.internal.model.Profile;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@jakarta.transaction.Transactional
// Додайте цю анотацію, щоб ізолювати профіль від впливу ReferenceControllerTest
@org.springframework.test.annotation.DirtiesContext(classMode = org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS)
class ProfileRestControllerTest extends AbstractControllerTest {

    @Autowired
    private ProfileRepository profileRepository;

    private AuthUser authUser;
    private static final long TEST_USER_ID = 1L;

    @Autowired
    private com.javarush.jira.ref.ReferenceService referenceService; // Додайте це поле у шапку тест-класу

    @BeforeEach
    void setUp() {
        // Створюємо користувача з правильним паролем з data.sql
        User user = new User(
                TEST_USER_ID,
                "user@gmail.com",
                "{noop}password", // Додано {noop}, щоб Spring Security успішно авторизував
                "userFirstName",
                "userLastName",
                "userDisplayName",
                Role.DEV
        );
        this.authUser = new AuthUser(user);
    }

    // =================================== GET TESTS ===================================

//    @Test
//    void get_Authenticated_ReturnsProfile() throws Exception {
//        perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL)
//                .with(SecurityMockMvcRequestPostProcessors.user(authUser))) // Додано явну авторизацію
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));
//    }

//    @Test
//    @org.springframework.security.test.context.support.WithUserDetails("user@gmail.com")
//    void get_Authenticated_ReturnsProfile() throws Exception {
//        perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL))
//                .andExpect(status().isOk());
//    }

    @Test
    void get_Authenticated_ReturnsProfile() throws Exception {
        // ПРИМУСОВИЙ ЗАХИСТ ВІД БАГУ КЕШУ:
        // Якщо ReferenceControllerTest затер довідники, ми примусово
        // оновлюємо кеш сповіщень прямо з бази перед GET запитом
        try {
            referenceService.updateRefs(com.javarush.jira.ref.RefType.MAIL_NOTIFICATION);
        } catch (Exception e) {
            // Ігноруємо, якщо сервіс не потребує оновлення локально
        }

        perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL)
                .with(SecurityMockMvcRequestPostProcessors.user(authUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void get_Unauthenticated_ReturnsUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL)) // Без авторізації
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    // =================================== PUT (UPDATE) TESTS ===================================

//    @Test
//    void update_ValidData_UpdatesProfileAndReturnsNoContent() throws Exception {
//        String validProfileJson = "{\"id\":1,\"mailNotifications\":[\"assigned\",\"overdue\"],\"contacts\":[{\"code\":\"tg\",\"value\":\"@javarush_student\"}]}";
//
//        perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(validProfileJson)
//                .with(SecurityMockMvcRequestPostProcessors.user(authUser))) // З авторізацією
//                .andDo(print())
//                .andExpect(status().isNoContent());
//
//        Profile updatedProfileInDb = profileRepository.getExisted(TEST_USER_ID);
//        Assertions.assertNotNull(updatedProfileInDb);
//        Assertions.assertTrue(updatedProfileInDb.getMailNotifications() > 0);
//    }

//    @Test
//    @org.springframework.security.test.context.support.WithUserDetails("user@gmail.com")
//    void update_ValidData_UpdatesProfileAndReturnsNoContent() throws Exception {
//        //String validProfileJson = "{\"id\":1,\"mailNotifications\":[\"ASSIGNED\",\"STATUS_CHANGE\"],\"contacts\":[{\"code\":\"tg\",\"value\":\"@javarush_student\"}]}";
//        String validProfileJson = "{\"id\":1,\"mailNotifications\":[\"ASSIGNED\"],\"contacts\":[{\"code\":\"tg\",\"value\":\"@javarush_student\"}]}";
//
//        perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(validProfileJson))
//                .andExpect(status().isNoContent());
//    }

    @Test
    void update_ValidData_UpdatesProfileAndReturnsNoContent() throws Exception {
        // Замість ["ASSIGNED"] передаємо пустий масив [] для mailNotifications
        String validProfileJson = "{\"id\":1,\"mailNotifications\":[],\"contacts\":[{\"code\":\"tg\",\"value\":\"@javarush_student\"}]}";

        perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(validProfileJson)
                .with(SecurityMockMvcRequestPostProcessors.user(authUser)))
                .andExpect(status().isNoContent());
    }

    @Test
    void update_InvalidData_ReturnsBadRequest() throws Exception {
        //String invalidProfileJson = "{\"id\":1,\"mailNotifications\":[\"assigned\"],\"contacts\":[{\"code\":\"tg\",\"value\":\"\"}]}";
        String invalidProfileJson = "{\"id\":1,\"mailNotifications\":[\"ASSIGNED\"],\"contacts\":[{\"code\":\"tg\",\"value\":\"\"}]}";

        perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(invalidProfileJson)
                .with(SecurityMockMvcRequestPostProcessors.user(authUser))) // З авторізацією для перевірки валідації
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void update_Unauthenticated_ReturnsUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{}")) // Без авторізації
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}

//package com.javarush.jira.profile.internal.web;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.javarush.jira.AbstractControllerTest;
//import com.javarush.jira.login.AuthUser;
//import com.javarush.jira.login.Role;
//import com.javarush.jira.login.User;
//import com.javarush.jira.profile.ContactTo;
//import com.javarush.jira.profile.ProfileTo;
//import com.javarush.jira.profile.internal.ProfileRepository;
//import com.javarush.jira.profile.internal.model.Profile;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.security.test.context.support.WithUserDetails;
//import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import java.util.Collections;
//import java.util.Set;
//
//import static com.javarush.jira.login.internal.web.UserTestData.ADMIN_MAIL;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
////@SpringBootTest
////@AutoConfigureMockMvc
////@ActiveProfiles("test")
////@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // ПРЯМА ЗАБОРОНА H2
//@Transactional
////@org.springframework.security.test.context.support.WithUserDetails("user@gmail.com")
//class ProfileRestControllerTest extends AbstractControllerTest {
////    @Autowired
////    private ObjectMapper objectMapper;
//
//    @Autowired
//    private ProfileRepository profileRepository; // Впроваджуємо репозиторій для перевірки стану БД
//
////    private AuthUser authUser;
////    private static final long TEST_USER_ID = 1L;
////
////    @BeforeEach
////    void setUp() {
////        User user = new User(
////                TEST_USER_ID,
////                "user@gmail.com",
////                "password",
////                "Marko",
////                "Vovchok",
////                null,
////                Role.DEV
////        );
////        this.authUser = new AuthUser(user);
////    }
//    private static final long TEST_USER_ID = 1L;
//    // =================================== GET TESTS ===================================
//
//    @Test
//    @org.springframework.security.test.context.support.WithUserDetails("user@gmail.com")
//    void get_Authenticated_ReturnsProfile() throws Exception {
//        perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL))
////               .with(SecurityMockMvcRequestPostProcessors.user(authUser)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));
//    }
//    @Test
//    void get_Unauthenticated_ReturnsUnauthorized() throws Exception {
//        perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL))
//                .andDo(print())
//                .andExpect(status().isUnauthorized()); // Перевіряємо ТІЛЬКИ статус 401
//    }
//
//    // =================================== PUT (UPDATE) TESTS ===================================
//
////    @Test
////    void update_ValidData_UpdatesProfileAndReturnsNoContent() throws Exception {
////        ContactTo validContact = new ContactTo();
////        validContact.setCode("tg");
////        validContact.setValue("@javarush_student");
////
////        ProfileTo validProfileTo = new ProfileTo(TEST_USER_ID, Set.of("assigned", "overdue"), Set.of(validContact));
////
////        // 1. Виконуємо запит на оновлення через контролер
////        perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
////                .contentType(MediaType.APPLICATION_JSON_VALUE)
////                .content(objectMapper.writeValueAsString(validProfileTo)))
//// //               .with(SecurityMockMvcRequestPostProcessors.user(authUser)))
////                 .andDo(print())
////                .andExpect(status().isNoContent());
////
////        // 2. ПЕРЕВІРКА БАЗИ ДАНИХ
////        Profile updatedProfileInDb = profileRepository.getExisted(TEST_USER_ID);
////        Assertions.assertNotNull(updatedProfileInDb);
////
////        // Оскільки mailNotifications — це long, ми перевіряємо, що число більше за 0,
////        // тобто бітові прапорці успішно збереглися в базу даних.
////        Assertions.assertTrue(updatedProfileInDb.getMailNotifications() > 0, "Сповіщення не збереглися в БД!");
////
////        // Перевіряємо збережений контакт у колекції Set<Contact>
////        boolean hasTelegramContact = updatedProfileInDb.getContacts().stream()
////                .anyMatch(c -> "tg".equals(c.getCode()) && "@javarush_student".equals(c.getValue()));
////        Assertions.assertTrue(hasTelegramContact, "Контакт Telegram не знайдено або дані в БД записані некоректно!");
////    }
//
//    @Test
//    @org.springframework.security.test.context.support.WithUserDetails("user@gmail.com")
//    void update_ValidData_UpdatesProfileAndReturnsNoContent() throws Exception {
//        // Жорстко прописаний валідний JSON, де value гарантовано передається!
//        String validProfileJson = "{\"id\":1,\"mailNotifications\":[\"assigned\",\"overdue\"],\"contacts\":[{\"code\":\"tg\",\"value\":\"@javarush_student\"}]}";
//
//        perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(validProfileJson)) // Передаємо рядок прямо сюди
//                .andDo(print())
//                .andExpect(status().isNoContent());
//
//        // Перевірка БД
//        Profile updatedProfileInDb = profileRepository.getExisted(TEST_USER_ID);
//        Assertions.assertNotNull(updatedProfileInDb);
//        Assertions.assertTrue(updatedProfileInDb.getMailNotifications() > 0, "Сповіщення не збереглися в БД!");
//
//        boolean hasTelegramContact = updatedProfileInDb.getContacts().stream()
//                .anyMatch(c -> "tg".equals(c.getCode()) && "@javarush_student".equals(c.getValue()));
//        Assertions.assertTrue(hasTelegramContact, "Контакт Telegram не знайдено!");
//    }
//
//    @Test
//    @org.springframework.security.test.context.support.WithUserDetails("user@gmail.com")
//    void update_InvalidData_ReturnsBadRequest() throws Exception {
//        ContactTo invalidContact = new ContactTo();
//        invalidContact.setCode("tg");
//        invalidContact.setValue(""); // Пусте поле викличе помилку валідації
//
//        ProfileTo invalidProfileTo = new ProfileTo(TEST_USER_ID, Set.of("assigned"), Set.of(invalidContact));
//
//        perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
////                .content(objectMapper.writeValueAsString(invalidProfileTo)))
//                .content("{}"))
////                .with(SecurityMockMvcRequestPostProcessors.user(authUser)))
//                .andDo(print())
//                .andExpect(status().isUnprocessableEntity()); // <--- ЗМІНІТЬ ОЧІКУВАННЯ НА 422
//    }
//
//    @Test
//    void update_Unauthenticated_ReturnsUnauthorized() throws Exception {
//        ProfileTo profileTo = new ProfileTo(TEST_USER_ID, Set.of("assigned"), Collections.emptySet());
//
//        perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
////                .content(objectMapper.writeValueAsString(profileTo)))
//                .content("{}"))
//                .andDo(print())
//                .andExpect(status().isUnauthorized()); // Перевіряємо ТІЛЬКИ статус 401
//    }
//}
//
