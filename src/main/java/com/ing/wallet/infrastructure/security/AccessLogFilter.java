package com.ing.wallet.infrastructure.security;

import com.ing.wallet.infrastructure.exception.ExceptionCodes;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AccessLogFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(AccessLogFilter.class);

    public AccessLogFilter() {
    }

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error(ExceptionCodes.INTERNAL_SERVER_ERROR.getMessage());
            throw e;
        }
    }
}