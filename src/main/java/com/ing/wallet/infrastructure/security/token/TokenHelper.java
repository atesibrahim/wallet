package com.ing.wallet.infrastructure.security.token;

import com.ing.wallet.infrastructure.exception.ExceptionCodes;
import com.ing.wallet.infrastructure.exception.WalletBusinessException;
import com.ing.wallet.infrastructure.utils.AuthUtils;
import com.nimbusds.jwt.JWTClaimNames;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TokenHelper {
    private static final Logger log = LoggerFactory.getLogger(TokenHelper.class);

    private final TokenService tokenService;

    public String getAccessToken() {
        final var authenticatedUser = AuthUtils.getUser();
        final var accessTokenDifference =
                getDifferenceBetweenExpireTimeAndNow(authenticatedUser.getProviderToken());
        final var timeUntilExpiration = 3600;
        if (accessTokenDifference > timeUntilExpiration) {
            return authenticatedUser.getProviderToken();
        }
        if (Objects.isNull(authenticatedUser.getProviderRefreshToken())) {
            throw new WalletBusinessException(ExceptionCodes.TOKEN_NULL_ERROR);
        }
        final var refreshTokenDifference =
                getDifferenceBetweenExpireTimeAndNow(authenticatedUser.getProviderRefreshToken());
        if (refreshTokenDifference <= 0) {
            tokenService.invalidateTokenByUser(authenticatedUser);
            log.error(ExceptionCodes.TOKEN_EXPIRED_ERROR.getMessage());
            throw new WalletBusinessException(ExceptionCodes.TOKEN_EXPIRED_ERROR);
        }

        final var user =
                authenticatedUser.toBuilder()
                        .providerToken("")
                        .providerRefreshToken("refreshTokenResponse.refreshToken()")
                        .build();
        tokenService.createToken(user);
        AuthUtils.authenticate(user);
        return user.getProviderToken();
    }

    private Long getDifferenceBetweenExpireTimeAndNow(final String token) {
        final var tokenExpireTimeAsTimestamp = getExpireTimeAsTimestamp(token);
        return ChronoUnit.SECONDS.between(
                Instant.now(), Instant.ofEpochSecond(tokenExpireTimeAsTimestamp));
    }

    private Long getExpireTimeAsTimestamp(final String token) {
        try {
            final var jwt = JWTParser.parse(token);
            final var exp =
                    ((SignedJWT) jwt)
                            .getPayload()
                            .toJSONObject()
                            .get(JWTClaimNames.EXPIRATION_TIME)
                            .toString();
            return Long.valueOf(exp);
        } catch (final ParseException e) {
            log.error(ExceptionCodes.TOKEN_JSON_ERROR.getMessage());
            throw new WalletBusinessException(ExceptionCodes.TOKEN_JSON_ERROR);
        }
    }
}