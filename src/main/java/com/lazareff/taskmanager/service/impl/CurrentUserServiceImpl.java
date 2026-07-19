package com.lazareff.taskmanager.service.impl;

import com.lazareff.taskmanager.entity.User;
import com.lazareff.taskmanager.exception.UserNotFoundException;
import com.lazareff.taskmanager.repository.UserRepository;
import com.lazareff.taskmanager.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserServiceImpl implements CurrentUserService {

    private final UserRepository userRepository;

    @Override
    public User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "Current user not found"
                        ));
    }
}
