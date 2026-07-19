package com.lazareff.taskmanager.dto.user;

import com.lazareff.taskmanager.enums.RoleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {

    @NotBlank(message = "First name cannot be blank")
    @Size(max = 255)
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 255)
    private String lastName;

    @NotNull(message = "Enabled is required")
    private Boolean enabled;

    @NotNull(message = "Role is required")
    private RoleType role;

}
