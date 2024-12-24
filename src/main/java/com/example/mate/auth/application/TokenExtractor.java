package com.example.mate.auth.application;

public interface TokenExtractor {

    Long extractAccessToken(String token);

    String extractRefreshToken(String token);
}
