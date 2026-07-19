package com.lazareff.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazareff.taskmanager.dto.auth.LoginRequest;
import com.lazareff.taskmanager.dto.auth.LoginResponse;
import com.lazareff.taskmanager.dto.auth.RegisterRequest;
import com.lazareff.taskmanager.dto.task.TaskCreateRequest;
import com.lazareff.taskmanager.dto.task.TaskUpdateRequest;
import com.lazareff.taskmanager.enums.TaskPriority;
import com.lazareff.taskmanager.enums.TaskStatus;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TaskControllerIT {

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

    private String registerAndLogin() throws Exception {

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("user@test.com");
        registerRequest.setPassword("password123");
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Smith");

        mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerRequest))
                )
                .andExpect(status().isOk());

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

        return response.getAccessToken();

    }

    @Test
    void create_shouldCreateTask() throws Exception {

        String token = registerAndLogin();

        TaskCreateRequest request = new TaskCreateRequest();
        request.setTitle("Integration Task");
        request.setDescription("Task created by integration test");
        request.setPriority(TaskPriority.HIGH);

        mockMvc.perform(
                        post("/tasks")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Integration Task"))
                .andExpect(jsonPath("$.description")
                        .value("Task created by integration test"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.status").value("TODO"));

    }

    @Test
    void getById_shouldReturnTask() throws Exception {

        String token = registerAndLogin();

        TaskCreateRequest request = new TaskCreateRequest();
        request.setTitle("My Task");
        request.setDescription("My Description");
        request.setPriority(TaskPriority.MEDIUM);

        String response = mockMvc.perform(
                        post("/tasks")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(response)
                .get("id")
                .asLong();

        mockMvc.perform(
                        get("/tasks/{id}", id)
                                .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("My Task"))
                .andExpect(jsonPath("$.description").value("My Description"))
                .andExpect(jsonPath("$.priority").value("MEDIUM"))
                .andExpect(jsonPath("$.status").value("TODO"));

    }

    @Test
    void getAll_shouldReturnUserTasks() throws Exception {

        String token = registerAndLogin();

        TaskCreateRequest request1 = new TaskCreateRequest();
        request1.setTitle("Task 1");
        request1.setDescription("Description 1");
        request1.setPriority(TaskPriority.HIGH);

        TaskCreateRequest request2 = new TaskCreateRequest();
        request2.setTitle("Task 2");
        request2.setDescription("Description 2");
        request2.setPriority(TaskPriority.LOW);

        mockMvc.perform(
                        post("/tasks")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request1))
                )
                .andExpect(status().isOk());

        mockMvc.perform(
                        post("/tasks")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request2))
                )
                .andExpect(status().isOk());

        mockMvc.perform(
                        get("/tasks")
                                .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].title").value("Task 2"));

    }

    @Test
    void update_shouldUpdateTask() throws Exception {

        String token = registerAndLogin();

        TaskCreateRequest createRequest = new TaskCreateRequest();
        createRequest.setTitle("Old Title");
        createRequest.setDescription("Old Description");
        createRequest.setPriority(TaskPriority.LOW);

        String response = mockMvc.perform(
                        post("/tasks")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createRequest))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(response)
                .get("id")
                .asLong();

        TaskUpdateRequest updateRequest = new TaskUpdateRequest();
        updateRequest.setTitle("New Title");
        updateRequest.setDescription("New Description");
        updateRequest.setPriority(TaskPriority.HIGH);
        updateRequest.setStatus(TaskStatus.IN_PROGRESS);

        mockMvc.perform(
                        put("/tasks/{id}", id)
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"))
                .andExpect(jsonPath("$.description").value("New Description"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

    }

    @Test
    void delete_shouldDeleteTask() throws Exception {

        String token = registerAndLogin();

        TaskCreateRequest request = new TaskCreateRequest();
        request.setTitle("Task");
        request.setDescription("Description");
        request.setPriority(TaskPriority.MEDIUM);

        String response = mockMvc.perform(
                        post("/tasks")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(response)
                .get("id")
                .asLong();

        mockMvc.perform(
                        delete("/tasks/{id}", id)
                                .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk());

        mockMvc.perform(
                        get("/tasks/{id}", id)
                                .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isNotFound());

    }

    @Test
    void shouldReturn403WithoutToken() throws Exception {

        TaskCreateRequest request = new TaskCreateRequest();
        request.setTitle("Task");
        request.setDescription("Description");
        request.setPriority(TaskPriority.HIGH);

        mockMvc.perform(
                        post("/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isForbidden());

    }

}