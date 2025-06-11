package com.example.mate.auth.infrastructure.jwt;

import com.example.mate.auth.application.TokenExtractor;
import com.example.mate.auth.exception.AuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import static com.example.mate.auth.exception.AuthExceptionType.*;

@Component
public class JwtExtractor implements TokenExtractor {

    private static final String USER_ID = "user_id";
    private static final String TOKEN_ID = "token_id";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";

    private final JwtParser jwtParser;

    public JwtExtractor(JwtProperties jwtProperties) {
        // JWT 비밀 키를 추출하여 SecretKey 객체로 변환
        SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
        // JWT 파서를 설정하고 빌드
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
    }

    @Override
    public Long extractAccessToken(String token) {
        return extract(token, ACCESS_TOKEN, USER_ID, Long.class);
    }

    @Override
    public String extractRefreshToken(String token) {
        return extract(token, REFRESH_TOKEN, TOKEN_ID, String.class);
    }

    private <T> T extract(String token, String expectedTokenType, String claimKey, Class<T> T) {
        Claims claims = parseClaim(token);
        String subject = claims.getSubject();
        T claimValue = claims.get(claimKey, T);
        if (claimValue != null && subject.equals(expectedTokenType)) {
            return claimValue;
        }
        throw new AuthException(INVALID_TOKEN_TYPE);
    }

    public Claims parseClaim(String token) {
        try {
            return jwtParser.parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            throw new AuthException(ALREADY_EXPIRED_TOKEN);
        } catch (Exception ex) {
            throw new AuthException(INVALID_TOKEN);
        }
    }
}
