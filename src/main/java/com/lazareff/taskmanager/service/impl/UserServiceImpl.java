package com.lazareff.taskmanager.service.impl;

import com.lazareff.taskmanager.dto.user.UserResponse;
import com.lazareff.taskmanager.dto.user.UserUpdateRequest;
import com.lazareff.taskmanager.entity.User;
import com.lazareff.taskmanager.exception.UserNotFoundException;
import com.lazareff.taskmanager.mapper.UserMapper;
import com.lazareff.taskmanager.repository.UserRepository;
import com.lazareff.taskmanager.service.RoleService;
import com.lazareff.taskmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleService roleService;

    @Override
    public UserResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found. Id: {}", id);

                    return new UserNotFoundException(
                            "User with id " + id + " not found"
                    );
                });

        return userMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> getAll() {

        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();

    }

    @Override
    public UserResponse update(Long id, UserUpdateRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User update failed. User not found. Id: {}", id);

                    return new UserNotFoundException(
                            "User with id " + id + " not found"
                    );
                });

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEnabled(request.getEnabled());

        user.setRole(
                roleService.findByRole(request.getRole())
        );

        userRepository.save(user);

        log.info("User updated successfully. Id: {}, Email: {}",
                user.getId(),
                user.getEmail());

        return userMapper.toResponse(user);

    }

    @Override
    public void delete(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User deletion failed. User not found. Id: {}", id);

                    return new UserNotFoundException(
                            "User with id " + id + " not found"
                    );
                });

        userRepository.delete(user);

        log.info("User deleted successfully. Id: {}, Email: {}",
                user.getId(),
                user.getEmail());

    }

}
