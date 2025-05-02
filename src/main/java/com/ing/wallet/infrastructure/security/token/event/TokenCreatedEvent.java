package com.ing.wallet.infrastructure.security.token.event;

import com.ing.wallet.application.dto.response.UserDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.security.oauth2.jwt.Jwt;

@Getter
public final class TokenCreatedEvent extends ApplicationEvent {

  private final UserDto user;
  private final Jwt token;

  public TokenCreatedEvent(final UserDto user, final Jwt token) {
    super(token);
    this.user = user;
    this.token = token;
  }
}
