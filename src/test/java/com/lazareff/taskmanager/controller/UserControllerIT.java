package com.lazareff.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazareff.taskmanager.dto.auth.LoginRequest;
import com.lazareff.taskmanager.dto.auth.LoginResponse;
import com.lazareff.taskmanager.dto.user.UserUpdateRequest;
import com.lazareff.taskmanager.entity.Role;
import com.lazareff.taskmanager.entity.User;
import com.lazareff.taskmanager.enums.RoleType;
import com.lazareff.taskmanager.repository.RefreshTokenRepository;
import com.lazareff.taskmanager.repository.RoleRepository;
import com.lazareff.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {

        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();

    }

    private String loginAsAdmin() throws Exception {

        Role adminRole = roleRepository.findByRole(RoleType.ADMIN)
                .orElseThrow();

        User admin = new User();

        admin.setEmail("admin@test.com");
        admin.setPassword(passwordEncoder.encode("password123"));
        admin.setFirstName("Admin");
        admin.setLastName("Admin");
        admin.setEnabled(true);
        admin.setRole(adminRole);

        userRepository.save(admin);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("admin@test.com");
        loginRequest.setPassword("password123");

        String json = mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        LoginResponse response =
                objectMapper.readValue(json, LoginResponse.class);

        return response.getAccessToken();

    }

    @Test
    void getById_shouldReturnUser() throws Exception {

        String token = loginAsAdmin();

        User admin = userRepository.findByEmail("admin@test.com")
                .orElseThrow();

        mockMvc.perform(
                        get("/users/{id}", admin.getId())
                                .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("admin@test.com"))
                .andExpect(jsonPath("$.firstName").value("Admin"))
                .andExpect(jsonPath("$.lastName").value("Admin"));

    }

    @Test
    void getAll_shouldReturnUsers() throws Exception {

        String token = loginAsAdmin();

        mockMvc.perform(
                        get("/users")
                                .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

    }

    @Test
    void update_shouldUpdateUser() throws Exception {

        String token = loginAsAdmin();

        User admin = userRepository.findByEmail("admin@test.com")
                .orElseThrow();

        UserUpdateRequest request = new UserUpdateRequest();
        request.setFirstName("Super");
        request.setLastName("Admin");
        request.setEnabled(true);
        request.setRole(RoleType.ADMIN);

        mockMvc.perform(
                        put("/users/{id}", admin.getId())
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Super"))
                .andExpect(jsonPath("$.lastName").value("Admin"));

    }

    @Test
    void delete_shouldDeleteUser() throws Exception {

        String token = loginAsAdmin();

        User admin = userRepository.findByEmail("admin@test.com")
                .orElseThrow();

        mockMvc.perform(
                        delete("/users/{id}", admin.getId())
                                .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk());

        assertFalse(
                userRepository.existsById(admin.getId())
        );

    }

    @Test
    void shouldReturn403ForRegularUser() throws Exception {

        Role userRole = roleRepository.findByRole(RoleType.USER)
                .orElseThrow();

        User user = new User();
        user.setEmail("user@test.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setFirstName("John");
        user.setLastName("Smith");
        user.setEnabled(true);
        user.setRole(userRole);

        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("user@test.com");
        loginRequest.setPassword("password123");

        String json = mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        LoginResponse response =
                objectMapper.readValue(json, LoginResponse.class);

        mockMvc.perform(
                        get("/users")
                                .header("Authorization",
                                        "Bearer " + response.getAccessToken())
                )
                .andExpect(status().isForbidden());

    }

}