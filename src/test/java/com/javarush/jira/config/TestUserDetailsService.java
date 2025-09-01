package com.javarush.jira.config;

import com.javarush.jira.login.AuthUser;
import com.javarush.jira.login.internal.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;

@TestConfiguration
@RequiredArgsConstructor
public class TestUserDetailsService {

    private final UserRepository userRepository;

    @Bean
    public UserDetailsService testUserDetailsService() {
        return email -> new AuthUser(userRepository.getExistedByEmail(email));
    }
}
