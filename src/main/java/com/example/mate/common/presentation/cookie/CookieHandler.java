package com.example.mate.common.presentation.cookie;

import jakarta.servlet.http.HttpServletRequest;
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
    private static final String LOCAL_PORT = "3000";

    private final CookieProperties properties;

    public ResponseCookie createCookie(String cookieKey, String cookieValue, HttpServletRequest request) {
        return createCookieWithMaxAge(cookieKey, cookieValue, properties.getMaxAge(), request);
    }

    public ResponseCookie createCookie(String cookieKey, String cookieValue, Long maxAge, HttpServletRequest request) {
        return createCookieWithMaxAge(cookieKey, cookieValue, maxAge, request);
    }

    public ResponseCookie deleteCookie(String cookieKey, HttpServletRequest request) {
        return createCookieWithMaxAge(cookieKey, DELETE_COOKIE_VALUE, DELETE_COOKIE_MAX_AGE, request);
    }

    private ResponseCookie createCookieWithMaxAge(String cookieKey, String cookieValue, Long maxAge,
                                                  HttpServletRequest request) {
        ResponseCookieBuilder cookieBuilder = ResponseCookie.from(cookieKey, cookieValue)
                .maxAge(maxAge)
                .path(properties.getPath())
                .sameSite(properties.getSameSite())
                .secure(properties.isSecure())
                .httpOnly(properties.isHttpOnly());

        String host = request.getHeader("Host");
        if (host != null && host.equals(LOCAL_HOST + ":" + LOCAL_PORT)) {
            cookieBuilder.domain(LOCAL_HOST);
        } else {
            cookieBuilder.domain(properties.getDomain());
        }
        return cookieBuilder.build();
    }

//    private ResponseCookie setDomainIfNotLocal(ResponseCookieBuilder cookieBuilder) {
//        if (!properties.getDomain().equalsIgnoreCase(LOCAL_HOST)) {
//            cookieBuilder.domain(properties.getDomain());
//        }
//        return cookieBuilder.build();
//    }
}
