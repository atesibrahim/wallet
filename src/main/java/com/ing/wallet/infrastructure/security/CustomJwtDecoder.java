package com.ing.wallet.infrastructure.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

@RequiredArgsConstructor
@Slf4j
public class CustomJwtDecoder implements JwtDecoder {

    private final JwtDecoder jwtDecoder;

    @Override
    public Jwt decode(final String token) throws JwtException {
        return jwtDecoder.decode(token);
    }
}