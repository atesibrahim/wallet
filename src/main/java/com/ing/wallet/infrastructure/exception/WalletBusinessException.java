package com.ing.wallet.infrastructure.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalletBusinessException extends RuntimeException {
    private final ExceptionCodes exceptionCode;
    private final String description;

    public WalletBusinessException(final ExceptionCodes exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
        this.description = null;
    }

    public WalletBusinessException(final ExceptionCodes exceptionCode, final String description) {
        super(description != null ? description : exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
        this.description = description;
    }

    public WalletBusinessException(final String message, final ExceptionCodes exceptionCode) {
        super(message);
        this.exceptionCode = exceptionCode;
        this.description = null;
    }
}
