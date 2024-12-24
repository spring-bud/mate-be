package com.example.mate.common.presentation.cookie;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieHandler {

    private static final Long DELETE_COOKIE_MAX_AGE = 0L;
    private static final String DELETE_COOKIE_VALUE = "";
    private static final String LOCAL_HOST = "localhost";

    private final CookieProperties properties;

    public ResponseCookie createCookie(String cookieKey, String cookieValue) {
        return createCookieWithMaxAge(cookieKey, cookieValue, properties.getMaxAge());
    }

    public ResponseCookie createCookie(String cookieKey, String cookieValue, Long maxAge) {
        return createCookieWithMaxAge(cookieKey, cookieValue, maxAge);
    }

    public ResponseCookie deleteCookie(String cookieKey) {
        return createCookieWithMaxAge(cookieKey, DELETE_COOKIE_VALUE, DELETE_COOKIE_MAX_AGE);
    }

    private ResponseCookie createCookieWithMaxAge(String cookieKey, String cookieValue, Long maxAge) {
        ResponseCookieBuilder cookieBuilder = ResponseCookie.from(cookieKey, cookieValue)
                .maxAge(maxAge)
                .path(properties.getPath())
                .sameSite(properties.getSameSite())
                .secure(properties.isSecure())
                .httpOnly(properties.isHttpOnly());
        return setDomainIfNotLocal(cookieBuilder);
    }

    private ResponseCookie setDomainIfNotLocal(ResponseCookieBuilder cookieBuilder) {
        if (!properties.getDomain().equalsIgnoreCase(LOCAL_HOST)) {
            cookieBuilder.domain(properties.getDomain());
        }
        return cookieBuilder.build();
    }
}
