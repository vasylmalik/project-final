package com.javarush.jira.mail.internal;

import com.javarush.jira.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "mail_case")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MailCase extends BaseEntity {
    @Column(name = "email", nullable = false, updatable = false)
    @NotBlank
    @Size(min = 2, max = 255)
    private String email;

    @Column(name = "name", nullable = false, updatable = false)
    @NotBlank
    @Size(min = 2, max = 255)
    private String name;

    @Column(name = "template", nullable = false, updatable = false)
    @NotBlank
    @Size(min = 2, max = 255)
    private String template;

    @Column(name = "result", nullable = false, updatable = false)
    @NotBlank
    @Size(min = 2, max = 255)
    private String result;

    @CreationTimestamp
    @Column(name = "date_time", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime dateTime;

    public MailCase(String email, String name, String template, String result) {
        this.email = email;
        this.name = name;
        this.template = template;
        this.result = result;
    }

    @Override
    public String toString() {
        return "MailCase{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", template='" + template + '\'' +
                ", result='" + result + '\'' +
                ", dateTime=" + dateTime +
                ", id=" + id +
                '}';
    }
}
