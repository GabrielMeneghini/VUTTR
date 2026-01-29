package com.apirest.vuttr.config.security;

import com.apirest.vuttr.exceptions.ErrorResponse;
import com.apirest.vuttr.repositories.UserRepository;
import com.apirest.vuttr.services.TokenService;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            var token = getToken(request);

            if(token != null) {
                var subject = tokenService.validJwt(token);
                var user = userRepository.findByEmail(subject).orElseThrow(() -> new UsernameNotFoundException("JWT subject not found or inactive."));

                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);
        } catch(JWTVerificationException | NoSuchElementException | UsernameNotFoundException e) {
            handleJwtError(request, response, e);
            return;
        }
    }

    private String getToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");

        if(authHeader != null) {
            if(authHeader.startsWith("Bearer ")) {
                return authHeader.substring(7);
            }
        }

        return null;
    }

    private void handleJwtError(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException {
        var status = HttpStatus.UNAUTHORIZED;
        String message;

        if(e instanceof UsernameNotFoundException) {
            message = e.getMessage();
        } else {
            message = "JWT is invalid or expired.";
        }

        ErrorResponse error = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setHeader("WWW-Authenticate",
                "Bearer realm=\"vuttr\", error=\"invalid_token\"");
        objectMapper.writeValue(response.getWriter(), error);
    }

}
