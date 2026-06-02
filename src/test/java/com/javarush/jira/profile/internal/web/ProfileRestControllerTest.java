package com.javarush.jira.profile.internal.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.login.AuthUser;
import com.javarush.jira.login.Role;
import com.javarush.jira.login.User;
import com.javarush.jira.profile.ContactTo;
import com.javarush.jira.profile.ProfileTo;
import com.javarush.jira.profile.internal.ProfileRepository;
import com.javarush.jira.profile.internal.model.Profile;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class ProfileRestControllerTest extends AbstractControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProfileRepository profileRepository; // Впроваджуємо репозиторій для перевірки стану БД

    private AuthUser authUser;
    private static final long TEST_USER_ID = 1L;

    @BeforeEach
    void setUp() {
        User user = new User(
                TEST_USER_ID,
                "user@gmail.com",
                "password",
                "Marko",
                "Vovchok",
                null,
                Role.DEV
        );
        this.authUser = new AuthUser(user);
    }

    // =================================== GET TESTS ===================================

    @Test
    void get_Authenticated_ReturnsProfile() throws Exception {
        perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL)
                .with(SecurityMockMvcRequestPostProcessors.user(authUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void get_Unauthenticated_ReturnsUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL))
                .andDo(print())
                .andExpect(status().isUnauthorized()); // Перевіряємо ТІЛЬКИ статус 401
    }

    // =================================== PUT (UPDATE) TESTS ===================================

    @Test
    void update_ValidData_UpdatesProfileAndReturnsNoContent() throws Exception {
        ContactTo validContact = new ContactTo();
        validContact.setCode("tg");
        validContact.setValue("@javarush_student");

        ProfileTo validProfileTo = new ProfileTo(TEST_USER_ID, Set.of("assigned", "overdue"), Set.of(validContact));

        // 1. Виконуємо запит на оновлення через контролер
        perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(validProfileTo))
                .with(SecurityMockMvcRequestPostProcessors.user(authUser)))
                .andDo(print())
                .andExpect(status().isNoContent());

        // 2. ПЕРЕВІРКА БАЗИ ДАНИХ
        Profile updatedProfileInDb = profileRepository.getExisted(TEST_USER_ID);
        Assertions.assertNotNull(updatedProfileInDb);

        // Оскільки mailNotifications — це long, ми перевіряємо, що число більше за 0,
        // тобто бітові прапорці успішно збереглися в базу даних.
        Assertions.assertTrue(updatedProfileInDb.getMailNotifications() > 0, "Сповіщення не збереглися в БД!");

        // Перевіряємо збережений контакт у колекції Set<Contact>
        boolean hasTelegramContact = updatedProfileInDb.getContacts().stream()
                .anyMatch(c -> "tg".equals(c.getCode()) && "@javarush_student".equals(c.getValue()));
        Assertions.assertTrue(hasTelegramContact, "Контакт Telegram не знайдено або дані в БД записані некоректно!");
    }

    @Test
    void update_InvalidData_ReturnsBadRequest() throws Exception {
        ContactTo invalidContact = new ContactTo();
        invalidContact.setCode("tg");
        invalidContact.setValue(""); // Пусте поле викличе помилку валідації

        ProfileTo invalidProfileTo = new ProfileTo(TEST_USER_ID, Set.of("assigned"), Set.of(invalidContact));

        perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(invalidProfileTo))
                .with(SecurityMockMvcRequestPostProcessors.user(authUser)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity()); // <--- ЗМІНІТЬ ОЧІКУВАННЯ НА 422
    }

    @Test
    void update_Unauthenticated_ReturnsUnauthorized() throws Exception {
        ProfileTo profileTo = new ProfileTo(TEST_USER_ID, Set.of("assigned"), Collections.emptySet());

        perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(profileTo)))
                .andDo(print())
                .andExpect(status().isUnauthorized()); // Перевіряємо ТІЛЬКИ статус 401
    }
}

