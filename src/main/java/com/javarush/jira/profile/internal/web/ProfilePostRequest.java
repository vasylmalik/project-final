package com.javarush.jira.profile.internal.web;

import com.javarush.jira.profile.ContactTo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;
import lombok.Data;

@Data
public class ProfilePostRequest {
    private Set<@NotBlank String> mailNotifications;

    private @Valid ContactTo[] contacts;
}
