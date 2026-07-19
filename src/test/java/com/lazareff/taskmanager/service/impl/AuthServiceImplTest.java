package com.lazareff.taskmanager.service.impl;

import com.lazareff.taskmanager.dto.auth.LoginRequest;
import com.lazareff.taskmanager.dto.auth.LoginResponse;
import com.lazareff.taskmanager.dto.auth.RegisterRequest;
import com.lazareff.taskmanager.entity.RefreshToken;
import com.lazareff.taskmanager.entity.Role;
import com.lazareff.taskmanager.entity.User;
import com.lazareff.taskmanager.enums.RoleType;
import com.lazareff.taskmanager.exception.EmailAlreadyExistsException;
import com.lazareff.taskmanager.exception.UserNotFoundException;
import com.lazareff.taskmanager.mapper.UserMapper;
import com.lazareff.taskmanager.repository.RefreshTokenRepository;
import com.lazareff.taskmanager.repository.UserRepository;
import com.lazareff.taskmanager.security.jwt.JwtService;
import com.lazareff.taskmanager.service.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.lazareff.taskmanager.dto.auth.RefreshTokenRequest;
import com.lazareff.taskmanager.exception.RefreshTokenExpiredException;
import com.lazareff.taskmanager.exception.RefreshTokenNotFoundException;
import com.lazareff.taskmanager.exception.RefreshTokenRevokedException;
import java.time.Instant;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void register_shouldRegisterUser() {

        RegisterRequest request = new RegisterRequest();
        request.setEmail("user@mail.com");
        request.setPassword("123456");
        request.setFirstName("John");
        request.setLastName("Smith");

        Role role = new Role();

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(passwordEncoder.encode(request.getPassword()))
                .thenReturn("encodedPassword");

        when(roleService.findByRole(RoleType.USER))
                .thenReturn(role);

        authService.register(request);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_shouldThrowEmailAlreadyExistsException() {

        RegisterRequest request = new RegisterRequest();
        request.setEmail("user@mail.com");

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(true);

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> authService.register(request)
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    void login_shouldReturnTokens() {

        LoginRequest request = new LoginRequest();
        request.setEmail("user@mail.com");
        request.setPassword("123456");

        User user = new User();
        user.setEmail(request.getEmail());

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));

        when(jwtService.generateAccessToken(user.getEmail()))
                .thenReturn("access-token");

        when(jwtService.generateRefreshToken(user.getEmail()))
                .thenReturn("refresh-token");

        when(jwtService.getRefreshTokenExpiryDate())
                .thenReturn(Instant.now().plusSeconds(3600));

        LoginResponse response = authService.login(request);

        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());

        verify(authenticationManager).authenticate(any());
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void login_shouldThrowUserNotFoundException() {

        LoginRequest request = new LoginRequest();
        request.setEmail("user@mail.com");
        request.setPassword("123456");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> authService.login(request)
        );
    }

    @Test
    void refreshToken_shouldThrowRefreshTokenExpiredException_whenTokenIsInvalid() {

        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("invalid-token");

        when(jwtService.isTokenValid("invalid-token"))
                .thenReturn(false);

        assertThrows(
                RefreshTokenExpiredException.class,
                () -> authService.refreshToken(request)
        );

        verify(refreshTokenRepository, never()).findByToken(any());
    }

    @Test
    void refreshToken_shouldThrowRefreshTokenNotFoundException() {

        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("token");

        when(jwtService.isTokenValid("token"))
                .thenReturn(true);

        when(refreshTokenRepository.findByToken("token"))
                .thenReturn(Optional.empty());

        assertThrows(
                RefreshTokenNotFoundException.class,
                () -> authService.refreshToken(request)
        );
    }

    @Test
    void refreshToken_shouldThrowRefreshTokenRevokedException() {

        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("token");

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRevoked(true);

        when(jwtService.isTokenValid("token"))
                .thenReturn(true);

        when(refreshTokenRepository.findByToken("token"))
                .thenReturn(Optional.of(refreshToken));

        assertThrows(
                RefreshTokenRevokedException.class,
                () -> authService.refreshToken(request)
        );
    }

    @Test
    void refreshToken_shouldThrowRefreshTokenExpiredException() {

        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("token");

        User user = new User();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().minusSeconds(10));

        when(jwtService.isTokenValid("token"))
                .thenReturn(true);

        when(refreshTokenRepository.findByToken("token"))
                .thenReturn(Optional.of(refreshToken));

        assertThrows(
                RefreshTokenExpiredException.class,
                () -> authService.refreshToken(request)
        );
    }

    @Test
    void refreshToken_shouldReturnNewTokens() {

        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("old-token");

        User user = new User();
        user.setEmail("user@mail.com");

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusSeconds(3600));

        when(jwtService.isTokenValid("old-token"))
                .thenReturn(true);

        when(refreshTokenRepository.findByToken("old-token"))
                .thenReturn(Optional.of(refreshToken));

        when(jwtService.generateRefreshToken(user.getEmail()))
                .thenReturn("new-refresh");

        when(jwtService.generateAccessToken(user.getEmail()))
                .thenReturn("new-access");

        when(jwtService.getRefreshTokenExpiryDate())
                .thenReturn(Instant.now().plusSeconds(7200));

        LoginResponse response = authService.refreshToken(request);

        assertEquals("new-access", response.getAccessToken());
        assertEquals("new-refresh", response.getRefreshToken());

        verify(refreshTokenRepository, times(2)).save(any(RefreshToken.class));
    }

    @Test
    void logout_shouldRevokeToken() {

        User user = new User();
        user.setId(1L);

        RefreshToken token = new RefreshToken();
        token.setUser(user);

        when(refreshTokenRepository.findByToken("token"))
                .thenReturn(Optional.of(token));

        authService.logout("token");

        assertTrue(token.isRevoked());

        verify(refreshTokenRepository).save(token);
    }

    @Test
    void logout_shouldThrowRefreshTokenNotFoundException() {

        when(refreshTokenRepository.findByToken("token"))
                .thenReturn(Optional.empty());

        assertThrows(
                RefreshTokenNotFoundException.class,
                () -> authService.logout("token")
        );
    }

}
