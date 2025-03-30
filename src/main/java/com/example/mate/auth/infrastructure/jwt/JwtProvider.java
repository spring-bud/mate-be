package com.example.mate.auth.infrastructure.jwt;

import com.example.mate.auth.application.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider implements TokenProvider {

    private static final String USER_ID = "user_id";
    private static final String USER_URL = "user_url";
    private static final String USER_NICKNAME = "user_nickname";
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
    public String generatedAccessToken(Long userId, String userProfileUrl, String userNickname) {
        Claims claims = generatedClaims(USER_ID, userId);
        claims.put(USER_URL, userProfileUrl);
        claims.put(USER_NICKNAME, userNickname);
        return generatedToken(claims, ACCESS_TOKEN, jwtProperties.getAccessExpired());
    }

    @Override
    public String generatedRefreshToken(String tokenId) {
        Claims claims = generatedClaims(TOKEN_ID, tokenId);
        return generatedToken(claims, REFRESH_TOKEN, jwtProperties.getRefreshExpired());
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

    // JWT 디코딩 및 Claims 추출 메서드 추가
    public Claims parseClaims(String token) throws JwtException {
        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(key) // 비밀키를 설정
                    .build()
                    .parseClaimsJws(token); // JWT를 파싱하여 Claims를 추출

            return jws.getBody();
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT token", e); // JWT 예외 처리
        }
    }

    // JWT에서 사용자 ID 추출
    public Long getUserIdFromAccessToken(String token) {
        Claims claims = parseClaims(token);
        return claims.containsKey(USER_ID) ? claims.get(USER_ID, Long.class) : null;
    }
}
