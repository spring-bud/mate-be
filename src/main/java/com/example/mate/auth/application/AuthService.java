package com.example.mate.auth.application;

import com.example.mate.auth.application.dto.TokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    public TokenResponseDto loginOrRegister(String oAuthId) {
        TokenResponseDto dto = new TokenResponseDto("aa", "bb");
        return dto;
    }
}
