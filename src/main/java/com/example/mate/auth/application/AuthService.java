package com.example.mate.auth.application;

import com.example.mate.auth.application.dto.TokenResponseDto;
import com.example.mate.auth.domain.Token;
import com.example.mate.auth.domain.repository.TokenRepository;
import com.example.mate.user.application.UserService;
import com.example.mate.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final TokenExtractor tokenExtractor;
    private final TokenRepository tokenRepository;


    public TokenResponseDto loginOrRegister(String oAuthId) {
        User findUser = userService.getOrRegisterByOAuthId(oAuthId);

        Token newToken = Token.builder()
                .userId(findUser.getId())
                .build();

        tokenRepository.save(newToken);

        String accessToken = tokenProvider.generatedAccessToken(newToken.getUserId());
        String refreshToken = tokenProvider.generatedRefreshToken(newToken.getTokenId());
        return TokenResponseDto.of(accessToken, refreshToken);
    }
}
