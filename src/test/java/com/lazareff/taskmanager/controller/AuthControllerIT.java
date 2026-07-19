package com.lazareff.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazareff.taskmanager.dto.auth.LoginRequest;
import com.lazareff.taskmanager.dto.auth.LoginResponse;
import com.lazareff.taskmanager.dto.auth.RefreshTokenRequest;
import com.lazareff.taskmanager.dto.auth.RegisterRequest;
import com.lazareff.taskmanager.repository.RefreshTokenRepository;
import com.lazareff.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void setUp() {

        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();

    }

    @Test
    void register_shouldRegisterUser() throws Exception {

        RegisterRequest request = new RegisterRequest();

        request.setEmail("user@test.com");
        request.setPassword("password123");
        request.setFirstName("John");
        request.setLastName("Smith");

        mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());

        assertTrue(
                userRepository.existsByEmail("user@test.com")
        );

    }

    @Test
    void login_shouldReturnTokens() throws Exception {

        RegisterRequest registerRequest = new RegisterRequest();

        registerRequest.setEmail("user@test.com");
        registerRequest.setPassword("password123");
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Smith");

        mockMvc.perform(
                post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest))
        );

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

        assertTrue(response.getAccessToken() != null);
        assertTrue(response.getRefreshToken() != null);

    }

    @Test
    void refresh_shouldReturnNewTokens() throws Exception {

        RegisterRequest registerRequest = new RegisterRequest();

        registerRequest.setEmail("user@test.com");
        registerRequest.setPassword("password123");
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Smith");

        mockMvc.perform(
                post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest))
        );

        LoginRequest loginRequest = new LoginRequest();

        loginRequest.setEmail("user@test.com");
        loginRequest.setPassword("password123");

        String loginJson = mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest))
                )
                .andReturn()
                .getResponse()
                .getContentAsString();

        LoginResponse loginResponse =
                objectMapper.readValue(loginJson, LoginResponse.class);

        RefreshTokenRequest refreshRequest =
                new RefreshTokenRequest();

        refreshRequest.setRefreshToken(
                loginResponse.getRefreshToken()
        );

        String refreshJson = mockMvc.perform(
                        post("/auth/refresh")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(refreshRequest))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        LoginResponse refreshResponse =
                objectMapper.readValue(refreshJson, LoginResponse.class);

        assertTrue(refreshResponse.getAccessToken() != null);
        assertTrue(refreshResponse.getRefreshToken() != null);

    }

    @Test
    void logout_shouldRevokeRefreshToken() throws Exception {

        RegisterRequest registerRequest = new RegisterRequest();

        registerRequest.setEmail("user@test.com");
        registerRequest.setPassword("password123");
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Smith");

        mockMvc.perform(
                post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest))
        );

        LoginRequest loginRequest = new LoginRequest();

        loginRequest.setEmail("user@test.com");
        loginRequest.setPassword("password123");

        String loginJson = mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest))
                )
                .andReturn()
                .getResponse()
                .getContentAsString();

        LoginResponse loginResponse =
                objectMapper.readValue(loginJson, LoginResponse.class);

        RefreshTokenRequest request =
                new RefreshTokenRequest();

        request.setRefreshToken(
                loginResponse.getRefreshToken()
        );

        mockMvc.perform(
                        post("/auth/logout")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());

        assertTrue(
                refreshTokenRepository
                        .findByToken(loginResponse.getRefreshToken())
                        .orElseThrow()
                        .isRevoked()
        );

    }

}
