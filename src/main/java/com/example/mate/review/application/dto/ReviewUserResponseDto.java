package com.example.mate.review.application.dto;

import com.example.mate.review.domain.Review;
import com.example.mate.user.domain.User;

public record ReviewUserResponseDto(
        Double star,
        String content,
        String nickname,
        String profileUrl
) {
    public static ReviewUserResponseDto of(Review review, User user) {
        return new ReviewUserResponseDto(
                review.getStar(),
                review.getContent(),
                user.getNickname(),
                user.getProfileUrl()
        );
    }
}


