package com.ing.wallet.infrastructure.security.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.wallet.authentication.application.dto.response.UserDto;
import com.ing.wallet.infrastructure.exception.ExceptionCodes;
import com.ing.wallet.infrastructure.exception.WalletBusinessException;
import com.ing.wallet.infrastructure.security.crypto.CryptoService;
import com.ing.wallet.infrastructure.security.token.event.TokenCreatedEvent;
import com.ing.wallet.infrastructure.security.token.event.TokenInvalidatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.security.GeneralSecurityException;
import java.time.Instant;

@Service
@Slf4j
public class JwtTokenService implements TokenService {

  private static final String CLAIMS_USER = "user";
  private final JwtProperties jwtProperties;
  private final ObjectMapper objectMapper;
  private final CryptoService cryptoService;
  private final JwtEncoder jwtEncoder;
  private final JwsHeader jwsHeader;

  private final ApplicationEventPublisher eventPublisher;

  public JwtTokenService(
      final JwtProperties jwtProperties,
      final ObjectMapper objectMapper,
      final CryptoService cryptoService,
      final JwtEncoder jwtEncoder,
      final ApplicationEventPublisher eventPublisher) {
    this.jwtProperties = jwtProperties;
    this.objectMapper = objectMapper;
    this.cryptoService = cryptoService;
    this.jwtEncoder = jwtEncoder;
    this.jwsHeader = JwsHeader.with(jwtProperties::getAlgorithm).build();
    this.eventPublisher = eventPublisher;
  }

  @Override
  public String createToken(final UserDto user) {
    final var claims = createClaims(user);
    final var jwt = jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims));
    eventPublisher.publishEvent(new TokenCreatedEvent(user, jwt));
    return jwt.getTokenValue();
  }

  @Override
  public void invalidateTokenByUser(final UserDto user) {
    eventPublisher.publishEvent(new TokenInvalidatedEvent(user.getUsername()));
  }

  @Override
  public UserDto getUser(final Object source) {
    final var claims = ((Jwt) source).getClaims();
    final var encryptedUser = (String) claims.get(CLAIMS_USER);
    return decryptAndDeserialize(encryptedUser);
  }

  private JwtClaimsSet createClaims(final UserDto user) {
    final var now = Instant.now();
    final var expiresAt = now.plus(jwtProperties.getExpiration());
    return JwtClaimsSet.builder()
        .issuer(jwtProperties.getIssuer())
        .issuedAt(now)
        .expiresAt(expiresAt)
        .subject(user.getEmail())
        .claim(CLAIMS_USER, serializeAndEncrypt(user))
        .build();
  }

  private String serializeAndEncrypt(final Object data) {
    try {
      final var json = objectMapper.writeValueAsString(data);
      return cryptoService.encrypt(json);
    } catch (final GeneralSecurityException e) {
      log.error("errorCode.getDescription(), e");
      throw new WalletBusinessException(ExceptionCodes.INTERNAL_SERVER_ERROR);
    } catch (final JsonProcessingException e) {
      log.error("errorCode.getDescription()", e);
      throw new WalletBusinessException(ExceptionCodes.TOKEN_JSON_ERROR);
    }
  }

  private UserDto decryptAndDeserialize(final String data) {
    try {
      final var json = cryptoService.decrypt(data);
      return objectMapper.readValue(json, UserDto.class);
    } catch (final GeneralSecurityException e) {
      log.error("errorCode.getDescription()", e);
      throw new WalletBusinessException(ExceptionCodes.INTERNAL_SERVER_ERROR);
    } catch (final JsonProcessingException e) {
      log.error("errorCode.getDescription()", e);
      throw new WalletBusinessException(ExceptionCodes.TOKEN_JSON_ERROR);
    }
  }
}
