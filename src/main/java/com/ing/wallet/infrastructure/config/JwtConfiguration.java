package com.ing.wallet.infrastructure.config;

import com.ing.wallet.infrastructure.security.CustomJwtDecoder;
import com.ing.wallet.infrastructure.security.token.JwtProperties;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
@Slf4j
public class JwtConfiguration {

    private final SecretKey secretKey;
    private final MacAlgorithm macAlgorithm;

    public JwtConfiguration(final JwtProperties jwtProperties) {
        final var secret = jwtProperties.getSecretKey().getBytes();
        secretKey = new SecretKeySpec(secret, jwtProperties.getAlgorithm());
        macAlgorithm = MacAlgorithm.valueOf(jwtProperties.getAlgorithm());
    }

    @Bean
    JwtDecoder jwtDecoder() {
        final var nimbusJwtDecoder =
                NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(macAlgorithm).build();
        return new CustomJwtDecoder(nimbusJwtDecoder);
    }

    @Bean
    JwtEncoder jwtEncoder() {
        final var jwks = new ImmutableSecret<>(secretKey);
        return new NimbusJwtEncoder(jwks);
    }
}
