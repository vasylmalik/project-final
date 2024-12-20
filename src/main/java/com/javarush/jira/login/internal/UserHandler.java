package com.javarush.jira.login.internal;

import com.javarush.jira.common.BaseHandler;
import com.javarush.jira.login.User;
import com.javarush.jira.login.UserDTO;
import org.springframework.stereotype.Component;

import java.util.function.BinaryOperator;

@Component
public class UserHandler extends BaseHandler<User, UserDTO, UserRepository, UserMapper> {
    public UserHandler(UserRepository repository, UserMapper mapper) {
        super(repository, mapper,
                UsersUtil::prepareForCreate,
                (BinaryOperator<User>) (user, dbUser) -> UsersUtil.prepareForUpdate(user, dbUser.getPassword()));
    }
}
