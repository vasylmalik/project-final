package com.javarush.jira.common.internal.config;

import com.javarush.jira.common.BaseRepository;
import com.javarush.jira.common.model.RefreshToken;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface RefreshTokenRepository extends BaseRepository<RefreshToken> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByUserEmail(String email);

    @Query("SELECT r.user.email FROM RefreshToken r WHERE r.token = :token")
    String findEmailByToken(String token);
}
