package com.example.mate.user.application.dto;

public record PopularityUserReviewResponseDto(
        long count,
        double star
) {
    public static PopularityUserReviewResponseDto of(long count, double star) {
        return new PopularityUserReviewResponseDto(
                count,
                star
        );
    }
}
