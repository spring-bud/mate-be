package com.example.mate.auth.application;

import com.example.mate.auth.application.dto.SocialDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthServiceKakao implements AuthService {
    @Value("${spring.kakao.api.key}")
    private String kakao_api_key;

    //@Value("${kakao.client.secret}")
    //private String kakao_client_secret;

    @Value("${spring.kakao.redirect.uri}")
    private String kakao_redirect_uri;

    private final static String KAKAO_AUTH_URI = "https://kauth.kakao.com";

    public String loginUrl() {
        return KAKAO_AUTH_URI + "/oauth/authorize"
                + "?client_id=" + kakao_api_key
                + "&redirect_uri=" + kakao_redirect_uri
                + "&response_type=code";
    }

    public String logoutUrl() {
        return KAKAO_AUTH_URI + "/oauth/logout"
                + "?client_id=" + kakao_api_key
                + "&redirect_uri=http://localhost:8080/api/v1/auth/logout";
    }

    public String getAccessToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakao_api_key);
        body.add("redirect_uri", kakao_redirect_uri);
        body.add("code", code);
        //body.add("client_secret", kakao_client_secret);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return (jsonNode.get("access_token")).asText();
    }

    public SocialDto getInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        // responseBody에 있는 정보 꺼내기
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        BigInteger id = jsonNode.get("id").bigIntegerValue();
        //String email = jsonNode.get("kakao_account").get("email").asText();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String image = jsonNode.get("properties")
                .get("profile_image").asText();

        return new SocialDto(id, nickname, image);
    }

    public void disconnect(String accessToken) {
        String reqURL = "https://kapi.kakao.com/v1/user/logout";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();
            System.out.println("로그아웃 : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String result = "";
            String line = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("result : " + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String loginOrRegister(SocialDto dto) {
        BigInteger socialId = dto.getsocialId();
        //사용자 여부 확인하고 없으면 회원가입 있으면 토큰 만들기로 넘어가기z

//        socialInfo socialInfo = socialInfoRepository.findsocialInfoByEmail(socialId);
//        if (socialInfo == null) {
//            //디비 추가하는 로직
//         아이디 정보 찾아오기
//        }
//
        //CustomUserInfoDto info = modelMapper.map(socialInfo, CustomUserInfoDto.class);
//        사용자 정보 담아서 토큰 만들기로 보내기
//        String accessToken = jwtUtil.createAccessToken(info);
        //return accessToken;
        return "";
    }
}
