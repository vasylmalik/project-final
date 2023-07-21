package com.javarush.jira.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.javarush.jira.common.to.BaseTo;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
public class ProfileTo extends BaseTo {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime lastLogin;

    @NotNull
    private Set<@NotBlank String> mailNotifications;

    @NotNull
    private Set<@Valid ContactTo> contacts;

    public ProfileTo(Long id, @Nullable Set<String> mailNotifications, @Nullable Set<@Valid ContactTo> contacts) {
        super(id);
        this.mailNotifications = mailNotifications == null ? Collections.emptySet() : Set.copyOf(mailNotifications);
        this.contacts = contacts == null ? Collections.emptySet() : Set.copyOf(contacts);
        this.lastLogin = null;
    }

    public boolean isContactTypePresent(String type) {
        return contacts.stream()
                .map(ContactTo::getCode)
                .anyMatch(s -> s.equals(type));
    }
}
