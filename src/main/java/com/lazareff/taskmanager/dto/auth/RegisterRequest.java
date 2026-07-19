package com.lazareff.taskmanager.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 100,
            message = "Password must be between 6 and 100 characters")
    private String password;

    @NotBlank(message = "First name cannot be blank")
    @Size(max = 255,
            message = "First name cannot be longer than 255 characters")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 255,
            message = "Last name cannot be longer than 255 characters")
    private String lastName;

}
