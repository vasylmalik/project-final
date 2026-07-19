package com.lazareff.taskmanager.mapper;

import com.lazareff.taskmanager.dto.user.UserResponse;
import com.lazareff.taskmanager.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "role.role", target = "role")
    UserResponse toResponse(User user);

}
