package com.lazareff.taskmanager.service;

import com.lazareff.taskmanager.entity.Role;
import com.lazareff.taskmanager.enums.RoleType;

public interface RoleService {

    Role findByRole(RoleType role);
}
