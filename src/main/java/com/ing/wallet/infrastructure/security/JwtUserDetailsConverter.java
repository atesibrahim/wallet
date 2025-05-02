package com.ing.wallet.infrastructure.security;

import com.ing.wallet.infrastructure.security.token.TokenService;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class JwtUserDetailsConverter implements Converter<Jwt, AbstractAuthenticationToken> {
  private final TokenService tokenService;

  public JwtUserDetailsConverter(@Lazy final TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @Override
  public AbstractAuthenticationToken convert(final Jwt source) {
    final var user = tokenService.getUser(source);
    return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
  }
}
