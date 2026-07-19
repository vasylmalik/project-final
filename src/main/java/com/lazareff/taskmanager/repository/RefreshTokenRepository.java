package com.lazareff.taskmanager.repository;

import com.lazareff.taskmanager.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void delete(RefreshToken refreshToken);
}
