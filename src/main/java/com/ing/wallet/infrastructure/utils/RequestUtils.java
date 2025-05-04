package com.ing.wallet.infrastructure.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class RequestUtils {
    private static final String RESPONSE_TOKEN = RequestUtils.class.getName() + ".RESPONSE_TOKEN";

    private RequestUtils() {
    }

    private static Optional<HttpServletRequest> getHttpServletRequest() {
        Optional var10000 = Optional.ofNullable(RequestContextHolder.getRequestAttributes());
        Objects.requireNonNull(ServletRequestAttributes.class);
        return var10000.map(ServletRequestAttributes.class::cast).map(attr -> ((ServletRequestAttributes) attr).getRequest());
    }

    public static Optional<String> getRequestScopedToken() {
        Optional var10000 = getHttpServletRequest().map((request) -> {
            return request.getAttribute(RESPONSE_TOKEN);
        });
        Objects.requireNonNull(String.class);
        return var10000.map(String.class::cast);
    }

    public static void setRequestScopedToken(final String token) {
        getHttpServletRequest().ifPresent((request) -> {
            request.setAttribute(RESPONSE_TOKEN, token);
        });
    }
}
