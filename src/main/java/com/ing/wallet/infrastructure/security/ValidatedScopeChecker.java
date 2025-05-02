package com.ing.wallet.infrastructure.security;

import com.ing.wallet.domain.entity.Customer;
import com.ing.wallet.domain.enums.AuthorityType;
import com.ing.wallet.infrastructure.exception.ExceptionCodes;
import com.ing.wallet.infrastructure.exception.WalletBusinessException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ValidatedScopeChecker implements AuthorizationManager<RequestAuthorizationContext> {
    private final Set<AuthorityType> authorities;

    public ValidatedScopeChecker(final AuthorityType... authorities) {
        this.authorities = new HashSet(Arrays.asList(authorities));
    }

    public AuthorizationDecision check(final Supplier<Authentication> authentication, final RequestAuthorizationContext object) {
        Set<String> userAuthorities = (Set)((Authentication)authentication.get()).getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        if (userAuthorities.contains("ROLE_ANONYMOUS")) {
            throw new WalletBusinessException(ExceptionCodes.ANONYMOUS_USER_ERROR);
        } else {
            Set<AuthorityType> userAuthoritiesEnum = (Set)userAuthorities.stream().map(AuthorityType::valueOf).collect(Collectors.toSet());
            boolean isAuthorized = userAuthoritiesEnum.containsAll(this.authorities);
            return new AuthorizationDecision(isAuthorized);
        }
    }
}
