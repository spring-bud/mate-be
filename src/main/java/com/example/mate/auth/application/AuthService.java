package com.example.mate.auth.application;

import com.example.mate.auth.application.dto.SocialDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;


@Service
public interface AuthService {
    String getAccessToken(String code) throws JsonProcessingException;

    SocialDto getInfo(String accessToken);

    String loginUrl();

    String logoutUrl();

    void disconnect(String accessToken);
}
