package com.ing.wallet.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.wallet.infrastructure.utils.AuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private static final Logger log = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    private final ObjectMapper objectMapper;

    public CustomAccessDeniedHandler(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final AccessDeniedException accessDeniedException)
            throws IOException {
        final var user = AuthUtils.getUser();
        log.error(
                "User: [{}] requires higher authorities for request to [{}] endpoint.",
                user.getUsername(),
                request.getRequestURI());
        response.addHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer");
        response.addHeader(
                HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
                checkUrlIsValidAndReturn(request.getHeader("origin")));
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        String errorResponse = "ACCESS_DENIED: endpoint requires higher authorities";
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    private String checkUrlIsValidAndReturn(final String url) {

        try {
            new URL(url).toURI();
            return url;
        } catch (Exception e) {
            log.error("Request Header Origin has Invalid URL");
            return "";
        }
    }
}