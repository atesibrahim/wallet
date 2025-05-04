package com.ing.wallet.infrastructure.exception;

import com.ing.wallet.authentication.application.dto.response.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionCodes {
    AUTHENTICATION_ERROR("A1", "Authentication failed"),
    AUTHORIZATION_ERROR("A2", "Authorization failed"),
    ACCESS_DENIED_ERROR("A3", "Access denied"),
    EMAIL_OR_PASSWORD_ERROR("A4", "Email or password is incorrect"),
    EMAIL_NOT_FOUND_ERROR("A5", "Email not found"),
    INTERNAL_SERVER_ERROR("I1", "Internal server error"),
    BAD_REQUEST("B1", "Bad request"),
    ANONYMOUS_USER_ERROR("A5", "Anonymous user error"),
    TOKEN_JSON_ERROR("T1", "Token parse error"),
    TOKEN_EXPIRED_ERROR("T2", "Token expired error"),
    TOKEN_NULL_ERROR("T3", "Token null error"),

    AUTHORITY_INVALID_ERROR("A6", "Authority invalid error"),
    INVALID_TRANSACTION_TYPE("T4", "Invalid transaction type"),
    INVALID_TRANSACTION_STATUS("T5", "Invalid transaction status"),

    INVALID_REQUEST("R1", "Invalid request"),

    WALLET_NOT_FOUND("W1", "Wallet not found"),
    INSUFFICIENT_BALANCE("W2", "Insufficient balance"),
    INSUFFICIENT_USABLE_BALANCE("W3", "Insufficient usable balance"),

    CUSTOMER_NOT_FOUND("C1", "Customer not found"),

    TRANSACTION_NOT_FOUND("T1", "Transaction not found"),
    TRANSACTION_BALANCE_NOT_ENOUGH("T2", "Transaction balance not enough"),
    TRANSACTION_CURRENCY_NOT_MATCH("T3", "Transaction currency not match"),
    TRANSACTION_TYPE_NOT_MATCH("T4", "Transaction type not match"),
    TRANSACTION_STATUS_NOT_MATCH("T5", "Transaction status not match"),
    TRANSACTION_STATUS_SHOULD_BE_PENDING("T6", "Transaction status should be pending"),
    WALLET_NOT_ACTIVE_FOR_WITHDRAW("W4", "Wallet not active for withdraw"),
    WALLET_NOT_ACTIVE_FOR_SHOPPING("W5", "Wallet not active for deposit"),;

    private final String code;
    private final String message;

    public ErrorResponse toErrorResponse() {
        return new ErrorResponse(this.code, this.message);
    }
}
