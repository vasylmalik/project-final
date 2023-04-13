package com.javarush.jira.login.internal.web;

import com.fasterxml.jackson.annotation.JsonView;
import com.javarush.jira.common.util.validation.View;
import com.javarush.jira.login.User;
import com.javarush.jira.login.UserTo;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.javarush.jira.common.util.validation.ValidationUtil.checkNew;

@Slf4j
@Validated
@RestController
@RequestMapping(value = AdminUserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminUserController extends AbstractUserController {
    static final String REST_URL = "/api/admin/users";

    @GetMapping("/{id}")
    public User get(@PathVariable long id) {
        return super.get(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // getByEmail with return old user until expired
    public void delete(@PathVariable long id) {
        super.delete(id);
    }

    @GetMapping
    public List<User> getAll() {
        return super.getAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createWithLocation(@Validated(View.OnCreate.class) @RequestBody User user) {
        log.info("create {}", user);
        checkNew(user);
        User created = super.create(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "users", key = "#user.email")
    // In case of update email, getByEmail with old email return old user until expired
    @JsonView(View.OnUpdate.class)
    public void update(@Validated(View.OnUpdate.class) @RequestBody User user, @PathVariable long id) {
        super.update(user, id);
    }

    @GetMapping("/by-email")
    public User getByEmail(@RequestParam String email) {
        return super.getByEmail(email);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // getByEmail with return old user until expired
    public void enable(@PathVariable long id, @RequestParam boolean enabled) {
        super.enable(id, enabled);
    }

    @PostMapping("/{id}/change_password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@RequestParam String oldPassword, @Size(min = 5, max = 128) @RequestParam String newPassword, @PathVariable long id) {
        changePassword0(oldPassword, newPassword, id);
    }

    @PostMapping("/form")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Hidden
    public void createOrUpdate(@Validated(View.OnUpdate.class) UserTo userTo) {
        if (userTo.isNew()) {
            super.create(userTo);
        } else {
            super.update(userTo, userTo.id());
        }
    }
}
