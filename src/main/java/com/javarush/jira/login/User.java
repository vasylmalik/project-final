package com.javarush.jira.login;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.javarush.jira.common.HasIdAndEmail;
import com.javarush.jira.common.model.TimestampEntry;
import com.javarush.jira.common.util.validation.NoHtml;
import com.javarush.jira.common.util.validation.View;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User extends TimestampEntry implements HasIdAndEmail, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @NoHtml
    @Size(max = 32)
    @Nullable
    @Column(name = "display_name", nullable = false, unique = true)
    String displayName;
    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotBlank
    @Size(max = 128)
    @NoHtml   // https://stackoverflow.com/questions/17480809
    private String email;
    @Column(name = "password")
    @NotBlank(groups = {View.OnCreate.class})
    @Size(min = 5, max = 128, groups = {View.OnCreate.class})
    // https://stackoverflow.com/a/12505165/548473
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonView(View.OnCreate.class)
    private String password;
    @NotBlank
    @Size(min = 2, max = 32)
    @NoHtml
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Size(max = 32)
    @NoHtml
    @Column(name = "last_name")
    @Nullable
    private String lastName;
    @CollectionTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role"}, name = "uk_user_role"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    @JoinColumn
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Role> roles;

    public User(User user) {
        this(user.id, user.email, user.password, user.firstName, user.lastName, user.displayName,
                user.startpoint, user.endpoint, user.roles);
    }

    public User(Long id, String email, String password, String firstName, String lastName, String displayName,
                Role... roles) {
        this(id, email, password, firstName, lastName, displayName,
                LocalDateTime.now(), null, Arrays.asList(roles));
    }

    public User(Long id, String email, String password, String firstName, String lastName, String displayName,
                LocalDateTime startpoint, LocalDateTime endpoint, Collection<Role> roles) {
        super(id, startpoint, endpoint);
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.displayName = displayName;
        setRoles(roles);
        normalize();
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = CollectionUtils.isEmpty(roles) ? EnumSet.noneOf(Role.class) : EnumSet.copyOf(roles);
    }

    public void normalize() {
        email = email.toLowerCase();
        if (!StringUtils.hasText(displayName)) {
            displayName = (lastName != null) ? firstName.charAt(0) + lastName : firstName;
        }
    }

    @Override
    public String toString() {
        return "User:" + id + '[' + email + ']';
    }

    public boolean hasRole(Role role) {
        return roles != null && roles.contains(role);
    }
}
