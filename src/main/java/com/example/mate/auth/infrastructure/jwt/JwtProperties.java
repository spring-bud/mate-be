package com.example.mate.auth.infrastructure.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

    private String secretKey;
    private Long accessExpired;
    private Long refreshExpire;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Long getAccessExpired() {
        return accessExpired;
    }

    public void setAccessExpired(Long accessExpired) {
        this.accessExpired = accessExpired;
    }

    public Long getRefreshExpire() {
        return refreshExpire;
    }

    public void setRefreshExpire(Long refreshExpire) {
        this.refreshExpire = refreshExpire;
    }
}