package com.ing.wallet.infrastructure.constant;

import com.ing.wallet.infrastructure.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.header.HeaderWriter;


public enum AuthorizationHeaderWriter implements HeaderWriter {
    INSTANCE {
        public void writeHeaders(final HttpServletRequest request, final HttpServletResponse response) {
            RequestUtils.getRequestScopedToken().ifPresent((token) -> {
                response.setHeader("Authorization", "Bearer " + token);
            });
        }
    };

    AuthorizationHeaderWriter() {
    }
}
