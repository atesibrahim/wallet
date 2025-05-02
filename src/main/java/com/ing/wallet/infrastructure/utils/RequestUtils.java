package com.ing.wallet.infrastructure.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class RequestUtils {
    private static final String AUTHORIZATION = "Authorization";
    private static final String FORWARDED_FOR = "X-Forwarded-For";
    private static final String ACCEPT = "Accept";
    private static final String PORTAL_CLIENT_ACCEPT_HEADER = "application/json, text/plain, */*";
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

    public static String getClientIPAddress() {
        return (String)getHttpServletRequest().map(RequestUtils::getClientIPAddress).orElseThrow();
    }

    public static String getClientIPAddress(final HttpServletRequest httpServletRequest) {
        return (String)Optional.of(httpServletRequest).map((request) -> {
            String forwardedFor = request.getHeader(FORWARDED_FOR);
            return (String)Objects.requireNonNullElse(forwardedFor, request.getRemoteAddr());
        }).orElseThrow();
    }

    public static Map<String, String> getHeadersWithAuthorizationForPortalClient(String accessToken) {
        return Map.of(AUTHORIZATION, "Bearer " + accessToken, ACCEPT, "application/json, text/plain, */*");
    }
}
