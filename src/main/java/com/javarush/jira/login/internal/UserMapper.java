package com.javarush.jira.login.internal;

import com.javarush.jira.common.BaseMapper;
import com.javarush.jira.login.Role;
import com.javarush.jira.login.User;
import com.javarush.jira.login.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.EnumSet;

@Mapper(componentModel = "spring", imports = {EnumSet.class, Role.class})
public interface UserMapper extends BaseMapper<User, UserDTO> {
    @Override
    @Mapping(target = "roles", expression = "java(EnumSet.of(Role.DEV))")
    User toEntity(UserDTO to);

    @Override
    @Mapping(target = "password", ignore = true)
    User updateFromDTO(UserDTO to, @MappingTarget User entity);
}
