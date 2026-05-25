package com.javarush.jira.profile.internal.web;

import com.javarush.jira.login.AuthUser;
import com.javarush.jira.profile.ProfileTo;
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
    public ProfileTo get(@AuthenticationPrincipal AuthUser authUser) {
        return super.get(authUser.id());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody ProfileTo profileTo, @AuthenticationPrincipal AuthUser authUser) {
        super.update(profileTo, authUser.id());
    }
}

