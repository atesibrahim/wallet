package com.ing.wallet.infrastructure.utils;

import com.ing.wallet.infrastructure.exception.ExceptionCodes;
import com.ing.wallet.infrastructure.exception.WalletBusinessException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class LogUtils {

    public static WalletBusinessException logAndThrowError(ExceptionCodes exceptionCodes) {
        log.error(exceptionCodes.getMessage());
        throw new WalletBusinessException(exceptionCodes);
    }
}
