package com.example.mate.auth.application;

import com.example.mate.auth.application.dto.SocialDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

@Slf4j
@Component
public class Jwtutil {
    private final Key key;
    private final long accessTokenExpTime;

    public Jwtutil(
            @Value("${spring.jwt.secret}") String secretKey,
            @Value("${spring.jwt.expiration_time}") long accessTokenExpTime
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
    }

    /**
     * Access Token 생성
     *
     * @param member
     * @return Access Token String
     */
    public String createAccessToken(SocialDto member) {
        return createToken(member, accessTokenExpTime);
    }


    /**
     * JWT 생성
     *
     * @param member
     * @param expireTime
     * @return JWT String
     */
    private String createToken(SocialDto member, long expireTime) {
        //페이로드에 들어가는 정보
        Claims claims = Jwts.claims();
        claims.put("memberId", member.getUserId());
//        claims.put("email", member.getEmail());
//        claims.put("nickname", member.getNickname());
//        claims.put("image", member.getImage());

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(expireTime);   //만료시간


        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))              //발급일자
                .setExpiration(Date.from(tokenValidity.toInstant()))  //만료일자
                .signWith(key, SignatureAlgorithm.HS256)              //서명에 사용할 키와 알고리즘
                .compact();
    }


    /**
     * Token에서 User ID 추출
     *
     * @param token
     * @return User ID
     */
    public Long getUserId(String token) {
        return parseClaims(token).get("memberId", Long.class);
    }


    /**
     * JWT 검증
     *
     * @param token
     * @return IsValidate
     */
    public boolean validateToken(String token) {
        try {
            //jwt를 파싱하여 클레임을 추출
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }


    /**
     * JWT Claims 추출
     *
     * @param accessToken
     * @return JWT Claims
     */
    public Claims parseClaims(String accessToken) {
        try {
            //jwt를 파싱하여 클레임을 추출
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
