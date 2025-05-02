package com.ing.wallet.infrastructure.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityConfigConstants {
    public final String AUTH_BASE_PATH = "/wallet/v1/auth";
    public final String CUSTOMER_BASE_PATH = "/wallet/v1/customers";
    public final String WALLET_BASE_PATH = "/wallet/v1/wallets";
    public final String TRANSACTION_BASE_PATH = "/wallet/v1/transactions";

    public final String[] PUBLIC_ENDPOINTS = {
            AUTH_BASE_PATH,
            AUTH_BASE_PATH + "/login",
            "/h2-console",
            "/h2-console/**",
            "/actuator/info",
            "/actuator/health",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/v2/api-docs/**",
            "/swagger-resources/**"
    };

    public final String[] LOGGED_IN_ENDPOINTS_POST = {
            AUTH_BASE_PATH + "/logout",
            AUTH_BASE_PATH + "/**",
    };

    public final String[] CUSTOMER_ENDPOINTS_GET = {
            CUSTOMER_BASE_PATH + "/list",
    };

    public final String[] WALLET_ENDPOINTS_POST = {
            WALLET_BASE_PATH,
    };

    public final String[] WALLET_ENDPOINTS_GET = {
            WALLET_BASE_PATH,
            WALLET_BASE_PATH + "/**",
    };

    public final String[] TRANSACTION_ENDPOINTS_GET = {
            TRANSACTION_BASE_PATH + "/**",
    };

    public final String[] TRANSACTION_ENDPOINTS_POST = {
            TRANSACTION_BASE_PATH + "/deposit",
            TRANSACTION_BASE_PATH + "/withdraw",
    };

    public final String[] TRANSACTION_ENDPOINTS_APPROVE_POST = {
            TRANSACTION_BASE_PATH + "/approve",
    };
}