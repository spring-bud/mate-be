package com.example.mate.user.application.dto;

import com.example.mate.user.domain.User;
import com.example.mate.user.domain.UserStatus;

public record UserInfoResponseDto(
        Long userId,
        UserStatus userStatus,
        String nickname,
        String profileUrl,
        String jobType,
        Integer jobYear,
        String intro,
        String email,
        String contact,
        String githubUrl,
        String blogUrl
) {

    public static UserInfoResponseDto of(User user) {
        return new UserInfoResponseDto(
                user.getId(),
                user.getStatus(),
                user.getNickname(),
                user.getProfileUrl(),
                user.getJobType(),
                user.getJobYear(),
                user.getIntro(),
                user.getEmail(),
                user.getContact(),
                user.getGithubUrl(),
                user.getBlogUrl()
        );
    }
}
