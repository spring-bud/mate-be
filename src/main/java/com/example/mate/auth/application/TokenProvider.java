package com.example.mate.auth.application;

public interface TokenProvider {

    String generatedAccessToken(Long userId);

    String generatedRefreshToken(String tokenId);
}
