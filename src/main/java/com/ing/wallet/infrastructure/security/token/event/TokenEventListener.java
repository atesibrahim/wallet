package com.ing.wallet.infrastructure.security.token.event;

import com.ing.wallet.infrastructure.utils.RequestUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class TokenEventListener {

  @EventListener(TokenCreatedEvent.class)
  public void handleTokenCreatedEvent(final TokenCreatedEvent tokenCreatedEvent) {
    final var jwt = tokenCreatedEvent.getToken();
    final var username = jwt.getSubject();
    log.info(
        "Token has been created for the user '{}': [authorities: {}]",
        username,
        tokenCreatedEvent.getUser().getAuthorities());
    RequestUtils.setRequestScopedToken(jwt.getTokenValue());
  }
}
