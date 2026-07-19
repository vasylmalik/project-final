package com.lazareff.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazareff.taskmanager.dto.auth.LoginRequest;
import com.lazareff.taskmanager.dto.auth.LoginResponse;
import com.lazareff.taskmanager.dto.auth.RegisterRequest;
import com.lazareff.taskmanager.dto.comment.CommentCreateRequest;
import com.lazareff.taskmanager.dto.comment.CommentUpdateRequest;
import com.lazareff.taskmanager.dto.task.TaskCreateRequest;
import com.lazareff.taskmanager.enums.TaskPriority;
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
class CommentControllerIT {

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

    private Long createTask(String token) throws Exception {

        TaskCreateRequest request = new TaskCreateRequest();
        request.setTitle("Task");
        request.setDescription("Task Description");
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

        return objectMapper.readTree(response)
                .get("id")
                .asLong();

    }

    @Test
    void create_shouldCreateComment() throws Exception {

        String token = registerAndLogin();

        Long taskId = createTask(token);

        CommentCreateRequest request = new CommentCreateRequest();
        request.setContent("First Comment");

        mockMvc.perform(
                        post("/tasks/{taskId}/comments", taskId)
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("First Comment"));

    }

    @Test
    void getAllByTask_shouldReturnComments() throws Exception {

        String token = registerAndLogin();

        Long taskId = createTask(token);

        CommentCreateRequest request1 = new CommentCreateRequest();
        request1.setContent("Comment 1");

        CommentCreateRequest request2 = new CommentCreateRequest();
        request2.setContent("Comment 2");

        mockMvc.perform(
                post("/tasks/{taskId}/comments", taskId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1))
        ).andExpect(status().isOk());

        mockMvc.perform(
                post("/tasks/{taskId}/comments", taskId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2))
        ).andExpect(status().isOk());

        mockMvc.perform(
                        get("/tasks/{taskId}/comments", taskId)
                                .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].content").value("Comment 1"))
                .andExpect(jsonPath("$[1].content").value("Comment 2"));

    }

    @Test
    void update_shouldUpdateComment() throws Exception {

        String token = registerAndLogin();

        Long taskId = createTask(token);

        CommentCreateRequest createRequest = new CommentCreateRequest();
        createRequest.setContent("Old Comment");

        String response = mockMvc.perform(
                        post("/tasks/{taskId}/comments", taskId)
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createRequest))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long commentId = objectMapper.readTree(response)
                .get("id")
                .asLong();

        CommentUpdateRequest updateRequest = new CommentUpdateRequest();
        updateRequest.setContent("Updated Comment");

        mockMvc.perform(
                        put("/comments/{commentId}", commentId)
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Updated Comment"));

    }

    @Test
    void delete_shouldDeleteComment() throws Exception {

        String token = registerAndLogin();

        Long taskId = createTask(token);

        CommentCreateRequest request = new CommentCreateRequest();
        request.setContent("Delete me");

        String response = mockMvc.perform(
                        post("/tasks/{taskId}/comments", taskId)
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long commentId = objectMapper.readTree(response)
                .get("id")
                .asLong();

        mockMvc.perform(
                        delete("/comments/{commentId}", commentId)
                                .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk());

        CommentUpdateRequest updateRequest = new CommentUpdateRequest();
        updateRequest.setContent("Updated after delete");

        mockMvc.perform(
                        put("/comments/{commentId}", commentId)
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                )
                .andExpect(status().isNotFound());

    }

    @Test
    void shouldReturn403WithoutToken() throws Exception {

        CommentCreateRequest request = new CommentCreateRequest();
        request.setContent("Comment");

        mockMvc.perform(
                        post("/tasks/1/comments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isForbidden());

    }

}
