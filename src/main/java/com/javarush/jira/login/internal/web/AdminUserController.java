package com.javarush.jira.login.internal.web;

import com.fasterxml.jackson.annotation.JsonView;
import com.javarush.jira.common.util.validation.View;
import com.javarush.jira.login.User;
import com.javarush.jira.login.UserTo;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Size;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.javarush.jira.common.BaseHandler.createdResponse;

@Validated
@RestController
@RequestMapping(value = AdminUserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminUserController extends AbstractUserController {
    static final String REST_URL = "/api/admin/users";

    @GetMapping("/{id}")
    @Operation(
            summary = "Отримати користувача за ID",
            description = "Повертає об'єкт користувача за вказаним ID"
    )
    public User get(@PathVariable long id) {
        return handler.get(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Видалити користувача",
            description = "Видаляє користувача з бази даних за ID"
    )
    // getByEmail with return old user until expired
    public void delete(@PathVariable long id) {
        handler.delete(id);
    }

    @GetMapping
    @Operation(
            summary = "Отримати всіх користувачів",
            description = "Повертає список усіх користувачів, відсортований за email"
    )
    public List<User> getAll() {
        return handler.getAll(Sort.by(Sort.Direction.ASC, "email"));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Створити нового користувача",
            description = "Створює нового користувача на основі переданих даних"
    )
    public ResponseEntity<User> createWithLocation(@Validated(View.OnCreate.class) @RequestBody User user) {
        User created = handler.create(user);
        return createdResponse(REST_URL, created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "users", key = "#user.email")
    @Operation(
            summary = "Оновити дані користувача",
            description = "Оновлює дані користувача за його ID"
    )
    // In case of update email, getByEmail with old email return old user until expired
    @JsonView(View.OnUpdate.class)
    public void update(@Validated(View.OnUpdate.class) @RequestBody User user, @PathVariable long id) {
        handler.update(user, id);
    }

    @GetMapping("/by-email")
    @Operation(
            summary = "Отримати користувача за email",
            description = "Повертає користувача, знайденого за адресою електронної пошти"
    )
    public User getByEmail(@RequestParam String email) {
        return handler.getRepository().getExistedByEmail(email);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Активувати/деактивувати користувача",
            description = "Змінює статус активності користувача"
    )
    // getByEmail with return old user until expired
    public void enable(@PathVariable long id, @RequestParam boolean enabled) {
        handler.enable(id, enabled);
    }

    @PostMapping("/{id}/change_password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Змінити пароль користувача",
            description = "Оновлює пароль користувача, якщо вказано правильний поточний"
    )
    public void changePassword(@RequestParam String oldPassword, @Size(min = 5, max = 128) @RequestParam String newPassword, @PathVariable long id) {
        changePassword0(oldPassword, newPassword, id);
    }

    @PostMapping("/form")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Hidden
    public void createOrUpdate(@Validated(View.OnUpdate.class) UserTo userTo) {
        if (userTo.isNew()) {
            handler.createFromTo(userTo);
        } else {
            handler.updateFromTo(userTo, userTo.id());
        }
    }
}
