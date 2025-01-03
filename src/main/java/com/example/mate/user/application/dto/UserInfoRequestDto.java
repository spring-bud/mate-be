package com.example.mate.user.application.dto;

import com.example.mate.user.domain.UserJobType;
import com.example.mate.user.domain.UserStatus;

import java.util.List;

public record UserInfoRequestDto(
        Long userId,
        UserStatus userStatus,
        String nickname,
        String profileUrl,
        UserJobType jobType,
        Integer jobYear,
        String intro,
        String email,
        String contact,
        String githubUrl,
        String blogUrl,
        List<String> stacks
) {
}

