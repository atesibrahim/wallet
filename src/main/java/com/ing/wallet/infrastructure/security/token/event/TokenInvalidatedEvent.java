package com.ing.wallet.infrastructure.security.token.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TokenInvalidatedEvent extends ApplicationEvent {

  private final String username;

  public TokenInvalidatedEvent(final String username) {
    super(username);
    this.username = username;
  }
}
