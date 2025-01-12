package com.example.mate.product.application.dto;

public record ProductLikeReviewDto(
        Long likeCount,
        Long reviewCount,
        boolean likeStatus
) {
    public static ProductLikeReviewDto of(
            Long likeCount,
            Long reviewCount,
            boolean likeStatus
    ) {
        return new ProductLikeReviewDto(
                likeCount,
                reviewCount,
                likeStatus
        );
    }
}

