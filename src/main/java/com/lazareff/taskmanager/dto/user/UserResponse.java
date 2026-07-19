package com.lazareff.taskmanager.dto.user;

import com.lazareff.taskmanager.enums.RoleType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private RoleType role;

    private boolean enabled;
}
