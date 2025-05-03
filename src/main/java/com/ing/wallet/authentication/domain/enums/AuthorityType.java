package com.ing.wallet.authentication.domain.enums;

import com.ing.wallet.infrastructure.exception.ExceptionCodes;
import com.ing.wallet.infrastructure.exception.WalletBusinessException;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum AuthorityType implements GrantedAuthority {
    LOGGED_IN,
    CUSTOMER,
    ADMIN,
    USER;

    private int value;

    public static Set<AuthorityType> buildNewAuthorities(final Set<AuthorityType> currentAuthorities, final AuthorityType authority) {
        return Stream.of(Set.of(authority), currentAuthorities).flatMap(Collection::stream).collect(Collectors.toSet());
    }

    public String getAuthority() {
        return this.name();
    }

    AuthorityType() {
    }

    public static AuthorityType fromString(String authority) {
        if (authority == null || authority.isEmpty())
        {
            throw new IllegalArgumentException("Authority cannot be null or empty");
        }

        return Arrays.stream(AuthorityType.values()).filter(type -> type.name().equalsIgnoreCase(authority))
                .findFirst()
                .orElseThrow(() -> new WalletBusinessException(authority, ExceptionCodes.AUTHORITY_INVALID_ERROR)); }
}