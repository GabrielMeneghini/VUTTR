package com.apirest.vuttr.config.security;

import com.apirest.vuttr.exceptions.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        var status = HttpStatus.UNAUTHORIZED;

        log.debug("Unauthorized request to {}: {}",
                request.getRequestURI(),
                authException == null ? "(no exception provided)" : authException.getMessage());

        var errorResponse = new ErrorResponse(status.value(),
                status.getReasonPhrase(),
                "Authentication is required to access this resource.",
                request.getRequestURI()
        );

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setHeader("WWW-Authenticate",
                "Bearer realm=\"vuttr\", error=\"required_token\"");
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }

}
