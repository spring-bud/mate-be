package com.example.mate.auth.infrastructure.redis;

import com.example.mate.auth.domain.Token;
import com.example.mate.auth.domain.repository.TokenRepository;
import com.example.mate.common.exception.redis.RedisException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.example.mate.common.exception.redis.RedisExceptionType.DESERIALIZE_ERROR;
import static com.example.mate.common.exception.redis.RedisExceptionType.SERIALIZE_ERROR;

@Component
@RequiredArgsConstructor
public class RedisTokenRepository implements TokenRepository {

    private static final Long TTL = 10_080L;  // FIXME: 임시 TTL 10,080 분 (7일)
    private static final String TOKEN_PREFIX = "token:";

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(Token token) {
        String key = generateKey(token.getTokenId());
        String value = serializeToken(token);
        redisTemplate.opsForValue().set(key, value, TTL, TimeUnit.MINUTES);
    }

    @Override
    public void deleteByTokenId(String tokenId) {
        redisTemplate.delete(generateKey(tokenId));
    }

    @Override
    public Optional<Token> findByTokenId(String tokenId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(generateKey(tokenId)))
                .map(this::deserializeToken);
    }

    private String generateKey(String tokenId) {
        //문자열을 하나로 합친다 여기서는 TOKEN_PREFIX + tokenId값
        return combineToString(TOKEN_PREFIX, tokenId);
    }

    private String serializeToken(Token token) {
        try {
            return objectMapper.writeValueAsString(token);
        } catch (JsonProcessingException ex) {
            throw new RedisException(SERIALIZE_ERROR);
        }
    }

    private Token deserializeToken(String tokenJson) {
        try {
            return objectMapper.readValue(tokenJson, Token.class);
        } catch (JsonProcessingException ex) {
            throw new RedisException(DESERIALIZE_ERROR);
        }
    }

    public static String combineToString(Object... args) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object arg : args) {
            stringBuilder.append(Objects.toString(arg, ""));
        }
        return stringBuilder.toString();
    }
}
