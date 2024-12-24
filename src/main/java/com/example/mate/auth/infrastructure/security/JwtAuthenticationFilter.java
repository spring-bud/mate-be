package com.example.mate.auth.infrastructure.security;

import com.example.mate.auth.infrastructure.security.exception.SecurityException;
import com.example.mate.auth.infrastructure.security.exception.SecurityExceptionType;
import com.example.mate.auth.presentation.support.AuthHeaderExtractor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public JwtAuthenticationFilter(RequestMatcher requestMatcher) {
        super(requestMatcher);
    }

    //인증을 시도하는 메서드
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException, IOException, ServletException {
        String accessToken = AuthHeaderExtractor.extract(request)
                .orElseThrow(() -> new SecurityException(SecurityExceptionType.UNAUTHORIZED));

        JwtAuthenticationToken beforeToken = JwtAuthenticationToken.beforeOf(accessToken);

        return super.getAuthenticationManager().authenticate(beforeToken);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain,
            Authentication authResult
    ) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);

        filterChain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed
    ) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        super.getFailureHandler().onAuthenticationFailure(request, response, failed);
    }
}
