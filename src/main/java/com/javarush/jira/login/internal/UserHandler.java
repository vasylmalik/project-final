package com.javarush.jira.login.internal;

import com.javarush.jira.common.BaseHandler;
import com.javarush.jira.login.User;
import com.javarush.jira.login.UserTo;
import org.springframework.stereotype.Component;

import java.util.function.BinaryOperator;

@Component
public class UserHandler extends BaseHandler<User, UserTo, UserRepository, UserMapper> {
    public UserHandler(UserRepository repository, UserMapper mapper) {
        super(repository, mapper,
                UsersUtil::prepareForCreate,
                (BinaryOperator<User>) (user, dbUser) -> UsersUtil.prepareForUpdate(user, dbUser.getPassword()));
    }
}
