package com.javarush.jira.login.internal.web;

import com.javarush.jira.common.error.IllegalRequestDataException;
import com.javarush.jira.login.User;
import com.javarush.jira.login.UserTo;
import com.javarush.jira.login.internal.UniqueMailValidator;
import com.javarush.jira.login.internal.UserMapper;
import com.javarush.jira.login.internal.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.List;

import static com.javarush.jira.common.util.validation.ValidationUtil.assureIdConsistent;
import static com.javarush.jira.common.util.validation.ValidationUtil.checkNew;
import static com.javarush.jira.login.internal.config.SecurityConfig.PASSWORD_ENCODER;

public abstract class AbstractUserController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    protected UserRepository repository;
    @Autowired
    private UserMapper mapper;
    @Autowired
    private UniqueMailValidator emailValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        Object target = binder.getTarget();
        if (target != null && emailValidator.supports(target.getClass())) {
            binder.addValidators(emailValidator);
        }
    }

    public List<User> getAll() {
        log.info("get all");
        return repository.findAll(Sort.by(Sort.Direction.ASC, "email"));
    }

    public User get(long id) {
        log.info("get {}", id);
        return repository.getExisted(id);
    }

    public User getByEmail(String email) {
        log.info("get by email {}", email);
        return repository.getExistedByEmail(email);
    }

    public User create(User user) {
        log.info("create {}", user);
        return repository.prepareAndCreate(user);
    }

    public User create(UserTo userTo) {
        log.info("create {}", userTo);
        checkNew(userTo);
        return repository.prepareAndCreate(mapper.toEntity(userTo));
    }

    public void update(User user, long id) {
        log.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
        User dbUser = repository.getExisted(id);
        repository.prepareAndUpdate(user, dbUser.getPassword());
    }

    // In case of update email, getByEmail with old email return old user until expired
    public User update(UserTo userTo, long id) {
        log.info("update {} with id={}", userTo, id);
        assureIdConsistent(userTo, id);
        User dbUser = repository.getExisted(userTo.id());
        return repository.prepareAndUpdate(mapper.updateFromTo(dbUser, userTo), dbUser.getPassword());
    }

    public void delete(long id) {
        log.info("delete {}", id);
        repository.deleteExisted(id);
    }

    @Transactional
    // getByEmail with return old user until expired
    public void enable(long id, boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        User user = repository.getExisted(id);
        user.setEnabled(enabled);
        repository.save(user);
    }

    // redefine problem: A method overriding another method must not redefine the parameter constraint configuration
    public void changePassword0(String oldPassword, String newPassword, long id) {
        log.info("change password for user with id={}", id);
        User user = repository.getExisted(id);
        if (!PASSWORD_ENCODER.matches(oldPassword, user.getPassword())) {
            throw new IllegalRequestDataException("Wrong old password");
        }
        user.setPassword(PASSWORD_ENCODER.encode(newPassword));
        repository.save(user);
    }
}
