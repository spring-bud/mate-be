package com.example.mate.auth.presentation.support;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import java.util.Optional;

public class AuthHeaderExtractor {
    private static final String BEARER_PREFIX = "Bearer";

    public static Optional<String> extract(HttpServletRequest request) {
        
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (
                StringUtils.hasText(header) &&
                        header.startsWith(BEARER_PREFIX)
        ) {
            //토큰 값만 반환
            return Optional.of(header.substring(BEARER_PREFIX.length()).trim());
        }
        return Optional.empty();
    }
}
