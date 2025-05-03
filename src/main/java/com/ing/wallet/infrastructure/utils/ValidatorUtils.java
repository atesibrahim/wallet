package com.ing.wallet.infrastructure.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidatorUtils {

    public static boolean isValidIBAN(String iban) {
        if (iban == null || iban.isEmpty()) {
            return false;
        }

        if (iban.length() < 15 || iban.length() > 34) {
            return false;
        }

        if (!iban.matches("[A-Za-z0-9]+")) {
            return false;
        }

        String countryCode = iban.substring(0, 2);
        if (!countryCode.matches("[A-Z]{2}")) {
            return false;
        }

        String checkDigit = iban.substring(2, 4);
        if (!checkDigit.matches("\\d{2}")) {
            return false;
        }

        String accountNumber = iban.substring(4);
        return accountNumber.matches("\\d+");
    }
}
