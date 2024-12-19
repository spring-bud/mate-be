package com.example.mate.auth.infrastructure.jwt;

import com.example.mate.auth.application.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider implements TokenProvider {

    private static final String USER_ID = "user_id";
    private static final String TOKEN_ID = "token_id";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";

    private final SecretKey key;
    private final JwtProperties jwtProperties;

    public JwtProvider(JwtProperties jwtProperties) {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
        this.jwtProperties = jwtProperties;
    }

    @Override
    public String generatedAccessToken(Long userId) {
        Claims claims = generatedClaims(USER_ID, userId);
        return generatedToken(claims, ACCESS_TOKEN, jwtProperties.getAccessExpired());
    }

    @Override
    public String generatedRefreshToken(String tokenId) {
        Claims claims = generatedClaims(TOKEN_ID, tokenId);
        return generatedToken(claims, REFRESH_TOKEN, jwtProperties.getRefreshExpire());
    }

    private Claims generatedClaims(String key, Object value) {
        Claims claims = Jwts.claims();
        claims.put(key, value);
        return claims;
    }

    private String generatedToken(Claims claims, String subject, Long exp) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)                   //사용자 정의 데이터
                .setSubject(subject)                 //jwt 소유자 사용자의 식별자로 사용된다
                .setIssuedAt(new Date(now))          //토큰이 발급된 시간
                .setExpiration(new Date(now + exp))  //만료시간
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
