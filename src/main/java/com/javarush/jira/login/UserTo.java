package com.javarush.jira.login;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.javarush.jira.common.HasIdAndEmail;
import com.javarush.jira.common.to.BaseTo;
import com.javarush.jira.common.util.validation.NoHtml;
import com.javarush.jira.common.util.validation.View;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserTo extends BaseTo implements HasIdAndEmail, Serializable {

    @Email
    @NotBlank
    @Size(max = 128)
    @NoHtml  // https://stackoverflow.com/questions/17480809
    private String email;

    @NotBlank(groups = {View.OnCreate.class})
    @Size(min = 5, max = 32, groups = {View.OnCreate.class})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonView(View.OnCreate.class)
    @Schema(accessMode = Schema.AccessMode.WRITE_ONLY) // https://stackoverflow.com/a/28025008/548473
    private String password;

    @NotBlank
    @Size(min = 2, max = 32)
    @NoHtml
    private String firstName;

    @Size(max = 32)
    @NoHtml
    private String lastName;

    @Size(max = 64)
    @NoHtml
    private String displayName;

    public UserTo(Long id, String email, String password, String firstName, String lastName, String displayName) {
        super(id);
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "UserTo:" + id + '[' + email + ']';
    }
}
