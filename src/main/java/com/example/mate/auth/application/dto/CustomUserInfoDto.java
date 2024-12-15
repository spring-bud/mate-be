package com.example.mate.auth.application.dto;

import com.example.mate.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomUserInfoDto extends User {
    private Long id;

    private String kakaoId;

    private String nickname;

    private String profileUrl;

}
