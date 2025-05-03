package com.ing.wallet.infrastructure.utils;

import com.ing.wallet.authentication.application.dto.response.UserDto;
import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class AuthUtils {

    private static SecurityContext getSecurityContext() {
        return SecurityContextHolder.getContext();
    }

    private static Authentication getAuthentication() {
        return getSecurityContext().getAuthentication();
    }

    public static UserDto getUser() {
        Object principal = getAuthentication().getPrincipal();
        return principal.getClass().equals(UserDto.class) ? (UserDto)principal : null;
    }

    public static void authenticate(final UserDto user) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        getSecurityContext().setAuthentication(authentication);
    }
}
