package com.example.mate.auth.infrastructure.security.oauth;

import com.example.mate.auth.application.AuthService;
import com.example.mate.auth.application.dto.TokenResponseDto;
import com.example.mate.common.presentation.cookie.CookieHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@Component
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    private static final String REFRESH_TOKEN = "refresh_token";

    private final CookieHandler cookieHandler;
    private final AuthService authService;
    private final String successRedirectUrl;

    public OAuthSuccessHandler(
            CookieHandler cookieHandler,
            AuthService authService,
            @Value("${spring.security.oauth2.success-redirect}") String successRedirectUrl
    ) {
        this.cookieHandler = cookieHandler;
        this.authService = authService;
        this.successRedirectUrl = successRedirectUrl;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        String profileImageUrl = (String) properties.get("profile_image");

        TokenResponseDto tokenResponseDto = authService.loginOrRegister(oAuth2User.getName(), profileImageUrl);
        ResponseCookie cookie = cookieHandler.createCookie(
                REFRESH_TOKEN,
                tokenResponseDto.refreshToken()
        );
        response.addHeader(SET_COOKIE, cookie.toString());
        response.sendRedirect(successRedirectUrl);
    }
}
