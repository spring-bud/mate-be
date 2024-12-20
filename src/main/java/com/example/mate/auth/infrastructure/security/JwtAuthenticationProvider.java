package com.example.mate.auth.infrastructure.security;

import com.example.mate.auth.application.TokenExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import static org.springframework.security.oauth2.core.OAuth2ErrorCodes.INVALID_TOKEN;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final TokenExtractor tokenExtractor;

    @Override
    public Authentication authenticate(Authentication authentication) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        String accessToken = token.getAccessToken();
        Long userId = extractUserIdByAccessToken(accessToken);
        return JwtAuthenticationToken.afterOf(userId);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class
                .isAssignableFrom(authentication);
    }

    private Long extractUserIdByAccessToken(String accessToken) throws SecurityException {
        try {
            return tokenExtractor.extractAccessToken(accessToken);
        } catch (RuntimeException ex) {
            throw new SecurityException(INVALID_TOKEN);
        }
    }
}
