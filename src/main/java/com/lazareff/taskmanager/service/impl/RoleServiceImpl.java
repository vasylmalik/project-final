package com.lazareff.taskmanager.service.impl;

import com.lazareff.taskmanager.entity.Role;
import com.lazareff.taskmanager.enums.RoleType;
import com.lazareff.taskmanager.exception.RoleNotFoundException;
import com.lazareff.taskmanager.repository.RoleRepository;
import com.lazareff.taskmanager.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role findByRole(RoleType role) {

        return roleRepository.findByRole(role)
                .orElseThrow(() -> new RoleNotFoundException(
                        "Role " + role + " not found"
                ));

    }

}
