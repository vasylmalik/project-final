package com.lazareff.taskmanager.repository;

import com.lazareff.taskmanager.entity.Role;
import com.lazareff.taskmanager.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRole(RoleType role);
}
