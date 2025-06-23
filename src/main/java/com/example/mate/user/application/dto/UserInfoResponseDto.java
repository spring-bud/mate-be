package com.example.mate.user.application.dto;

import com.example.mate.user.domain.User;
import com.example.mate.user.domain.UserStack;

import java.util.List;

public record UserInfoResponseDto(
        Long userId,
        String nickname,
        String profileUrl,
        String jobType,
        Integer jobYear,
        String intro,
        String email,
        String contact,
        String githubUrl,
        String blogUrl,
        List<UserStackInfo> userStacks,
        Boolean infoActive
) {

    public static UserInfoResponseDto of(User user) {
        return new UserInfoResponseDto(
                user.getId(),
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
                        .toList(),
                user.isInfoActive()
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
