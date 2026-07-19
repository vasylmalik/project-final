package com.lazareff.taskmanager.service.impl;

import com.lazareff.taskmanager.dto.user.UserResponse;
import com.lazareff.taskmanager.dto.user.UserUpdateRequest;
import com.lazareff.taskmanager.entity.Role;
import com.lazareff.taskmanager.entity.User;
import com.lazareff.taskmanager.enums.RoleType;
import com.lazareff.taskmanager.exception.UserNotFoundException;
import com.lazareff.taskmanager.mapper.UserMapper;
import com.lazareff.taskmanager.repository.UserRepository;
import com.lazareff.taskmanager.service.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getById_shouldReturnUser() {

        User user = new User();
        user.setId(1L);

        UserResponse response = new UserResponse();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(userMapper.toResponse(user))
                .thenReturn(response);

        UserResponse result = userService.getById(1L);

        assertEquals(response, result);

        verify(userRepository).findById(1L);
        verify(userMapper).toResponse(user);
    }

    @Test
    void getById_shouldThrowUserNotFoundException() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.getById(1L)
        );
    }

    @Test
    void getAll_shouldReturnUsers() {

        User user = new User();

        UserResponse response = new UserResponse();

        when(userRepository.findAll())
                .thenReturn(List.of(user));

        when(userMapper.toResponse(user))
                .thenReturn(response);

        List<UserResponse> result = userService.getAll();

        assertEquals(1, result.size());

        verify(userRepository).findAll();
        verify(userMapper).toResponse(user);
    }

    @Test
    void update_shouldUpdateUser() {

        User user = new User();

        UserUpdateRequest request = new UserUpdateRequest();
        request.setFirstName("John");
        request.setLastName("Smith");
        request.setEnabled(true);
        request.setRole(RoleType.ADMIN);

        Role role = new Role();

        UserResponse response = new UserResponse();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(roleService.findByRole(RoleType.ADMIN))
                .thenReturn(role);

        when(userMapper.toResponse(user))
                .thenReturn(response);

        UserResponse result = userService.update(1L, request);

        assertEquals(response, result);

        assertEquals("John", user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertTrue(user.isEnabled());
        assertEquals(role, user.getRole());

        verify(userRepository).save(user);
    }

    @Test
    void update_shouldThrowUserNotFoundException() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.update(1L, new UserUpdateRequest())
        );
    }

    @Test
    void delete_shouldDeleteUser() {

        User user = new User();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        userService.delete(1L);

        verify(userRepository).delete(user);
    }

    @Test
    void delete_shouldThrowUserNotFoundException() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.delete(1L)
        );
    }
}
