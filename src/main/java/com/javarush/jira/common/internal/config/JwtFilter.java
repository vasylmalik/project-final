package com.javarush.jira.common.internal.config;

import com.javarush.jira.common.error.AccessTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static java.util.Objects.nonNull;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService JWTService;
    private final UserDetailsService userDetailsService;

    public JwtFilter(JwtService JWTService, @Lazy UserDetailsService userDetailsService) {
        this.JWTService = JWTService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authorization = request.getHeader("Authorization");
        try {
            if (nonNull(authorization) && authorization.startsWith("Bearer ")) {
                String token = authorization.substring(7);
                String email = JWTService.extractEmail(token);

                if (nonNull(email) && SecurityContextHolder.getContext().getAuthentication() == null) {

                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            userDetails.getPassword(),
                            userDetails.getAuthorities()
                    );

                    if (JWTService.isValid(token, email))
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(request, response);
        } catch (SignatureException exception) {
            request.setAttribute("exception", new AccessTokenException("Invalid access token"));
            request.getRequestDispatcher("/error/catch").forward(request, response);
        } catch (ExpiredJwtException exception) {
            request.setAttribute("exception", new AccessTokenException("Access token has expired"));
            request.getRequestDispatcher("/error/catch").forward(request, response);
        }
    }
}
