package com.javarush.jira.login.internal;

import com.javarush.jira.common.BaseRepository;
import com.javarush.jira.common.error.NotFoundException;
import com.javarush.jira.login.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.javarush.jira.login.internal.config.SecurityConfig.PASSWORD_ENCODER;

@Transactional(readOnly = true)
@CacheConfig(cacheNames = "users")
public interface UserRepository extends BaseRepository<User> {

    @Cacheable(key = "#email")
    @Query("SELECT u FROM User u WHERE u.email = LOWER(:email)")
    Optional<User> findByEmailIgnoreCase(String email);

    @Transactional
    @CachePut(key = "#user.email")
    default User prepareAndCreate(User user) {
        return prepareAndUpdate(user, PASSWORD_ENCODER.encode(user.getPassword()));
    }

    @Transactional
    @CacheEvict(key = "#user.email")
    default User prepareAndUpdate(User user, String encPassword) {
        user.setPassword(encPassword);
        user.normalize();
        return save(user);
    }

    default User getExistedByEmail(String email) {
        return findByEmailIgnoreCase(email).orElseThrow(() -> new NotFoundException("User with email=" + email + " not found"));
    }
}
