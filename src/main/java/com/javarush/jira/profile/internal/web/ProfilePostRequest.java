package com.javarush.jira.profile.internal.web;

import com.javarush.jira.profile.ContactDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class ProfilePostRequest {
    private Set<@NotBlank String> mailNotifications;

    private @Valid ContactDTO[] contacts;
}
