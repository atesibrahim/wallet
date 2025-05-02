package com.ing.wallet.infrastructure.security.crypto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("crypto")
public class CryptoProperties {
  private String secretKey;
  private String secretKeyAlgorithm;
  private String transformation;
  private int gcmTagLength;
  private int gcmIvLength;
}
