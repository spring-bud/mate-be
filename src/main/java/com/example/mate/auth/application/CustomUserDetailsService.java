package com.example.mate.auth.application;

import com.example.mate.auth.application.dto.CustomUserInfoDto;
import com.example.mate.auth.application.dto.SocialDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthService authService;
    //어떤 Object(Source Object)에 있는 필드 값들을 자동으로 원하는 Object(Destination Object)에 Mapping 시켜주는 라이브러리
    private final ModelMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        // 디비에 저장된 사용자 정보를 가지고 온다
        SocialDto socialDto = null;
//        Member member = memberRepository.findById(Long.parseLong(id))
//                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저가 없습니다."));

        // 카카오 사용자 정보를 DTO로 변환
        CustomUserInfoDto dto = mapper.map(socialDto, CustomUserInfoDto.class);

        // 카카오 아이디를 기반으로 기존 사용자 조회 (회원가입된 사용자라면)
        //todo생성하기
//        Member member = memberRepository.findByKakaoId(kakaoUserInfo.getId())
//                //다른 소셜 로그인 추가시 변경
//                .orElseThrow(() -> new UsernameNotFoundException("카카오 아이디에 해당하는 유저가 없습니다."));

        return new CustomUserDetails(dto);
    }
}
