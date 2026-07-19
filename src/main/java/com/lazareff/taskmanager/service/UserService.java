package com.lazareff.taskmanager.service;

import com.lazareff.taskmanager.dto.user.UserResponse;
import com.lazareff.taskmanager.dto.user.UserUpdateRequest;

import java.util.List;

public interface UserService {

    UserResponse getById(Long id);

    List<UserResponse> getAll();

    UserResponse update(Long id, UserUpdateRequest request);

    void delete(Long id);
}
