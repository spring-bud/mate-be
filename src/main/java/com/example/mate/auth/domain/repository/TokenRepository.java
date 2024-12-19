package com.example.mate.auth.domain.repository;

import com.example.mate.auth.domain.Token;

import java.util.Optional;

public interface TokenRepository {

    void save(Token token);

    void deleteByTokenId(String tokenId);

    Optional<Token> findByTokenId(String tokenId);
}
