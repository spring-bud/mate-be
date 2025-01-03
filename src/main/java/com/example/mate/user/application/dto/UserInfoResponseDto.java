package com.example.mate.user.application.dto;

import com.example.mate.user.domain.User;
import com.example.mate.user.domain.UserJobType;
import com.example.mate.user.domain.UserStack;
import com.example.mate.user.domain.UserStatus;

import java.util.List;

public record UserInfoResponseDto(
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
        List<UserStackInfo> userStacks
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
                user.getBlogUrl(),
                user.getUserStacks().stream()
                        .map(UserStackInfo::from)
                        .toList()
        );
    }

    public record UserStackInfo(
            Long stackId,
            String name
    ) {
        public static UserStackInfo from(UserStack userStack) {
            return new UserStackInfo(
                    userStack.getStack().getId(),
                    userStack.getStack().getName()
            );
        }
    }
}
