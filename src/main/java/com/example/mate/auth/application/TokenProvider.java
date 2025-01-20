package com.example.mate.auth.application;

public interface TokenProvider {

    String generatedAccessToken(Long userId, String userProfileUrl, String userNickname);

    String generatedRefreshToken(String tokenId);
}
