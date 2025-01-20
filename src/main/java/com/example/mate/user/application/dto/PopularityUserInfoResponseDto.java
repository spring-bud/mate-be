package com.example.mate.user.application.dto;

import com.example.mate.user.domain.User;
import com.example.mate.user.domain.UserJobType;
import com.example.mate.user.domain.UserStack;

import java.util.List;

public record PopularityUserInfoResponseDto(
        Long id,
        String nickname,
        String profileUrl,
        UserJobType jobType,
        long count,
        double star,
        List<UserStackInfo> userStacks

) {
    public static PopularityUserInfoResponseDto of(User user, PopularityUserReviewResponseDto userReviewInfo) {
        return new PopularityUserInfoResponseDto(
                user.getId(),
                user.getNickname(),
                user.getProfileUrl(),
                user.getJobType(),
                userReviewInfo.getCount(),
                userReviewInfo.getStar(),
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
