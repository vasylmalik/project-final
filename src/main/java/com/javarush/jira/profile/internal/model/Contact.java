package com.javarush.jira.profile.internal.model;

import com.javarush.jira.common.HasId;
import com.javarush.jira.common.util.validation.Code;
import com.javarush.jira.common.util.validation.NoHtml;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * id is the same as User.id (not autogenerate)
 */
@Entity
@Table(name = "contact")
@IdClass(Contact.ContactId.class)
@NoArgsConstructor
@Getter
@Setter
public class Contact implements HasId {
    @NotNull
    @Column(name = "id")
    private Long id;

    @Id
    @ManyToOne
    @JoinColumn(name = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Transient
    private Profile profile;

    // link to Reference.code with RefType.CONTACT
    @Id
    @Code
    @Column(name = "code", nullable = false)
    private String code;

    @NotBlank
    @Size(min = 2, max = 256)
    @Column(name = "value", nullable = false)
    @NoHtml
    private String value;

    public Contact(long id, String code, String value) {
        this.id = id;
        this.code = code;
        this.value = value;
    }

    @Data
    @NoArgsConstructor
    static class ContactId implements Serializable {
        private long id;
        private String code;
    }
}
