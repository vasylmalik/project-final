package com.javarush.jira.profile.internal.model;

import com.javarush.jira.common.HasId;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * id is the same as User.id (not autogenerate)
 */
@Entity
@Table(name = "profile")
@NoArgsConstructor
@Getter
@Setter
public class Profile implements HasId {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "last_failed_login")
    private LocalDateTime lastFailedLogin;

    @Column(name = "mail_notifications")
    private long mailNotifications;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    @JoinColumn(name = "id", updatable = false)
    private Set<Contact> contacts = new HashSet<>();

    public Profile(long id) {
        this.id = id;
    }

    public boolean hasNotification(long mask) {
        return (mailNotifications & mask) != 0;
    }

    @Override
    public String toString() {
        return "Profile: " + id;
    }
}
