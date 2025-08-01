package com.javarush.jira.profile.internal.web;

import com.javarush.jira.login.AuthUser;
import com.javarush.jira.profile.ProfileTo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = ProfileRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileRestController extends AbstractProfileController {
    public static final String REST_URL = "/api/profile";

    @GetMapping
    @Operation(
            summary = "Отримати профіль користувача",
            description = "Повертає дані профілю поточного автентифікованого користувача"
    )
    public ProfileTo get(@AuthenticationPrincipal AuthUser authUser) {
        return super.get(authUser.id());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Оновити профіль користувача",
            description = "Оновлює профіль поточного автентифікованого користувача"
    )
    public void update(@Valid @RequestBody ProfileTo profileTo, @AuthenticationPrincipal AuthUser authUser) {
        super.update(profileTo, authUser.id());
    }
}

