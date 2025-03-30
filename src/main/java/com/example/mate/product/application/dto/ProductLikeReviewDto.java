package com.example.mate.product.application.dto;

public record ProductLikeReviewDto(
        Long likeCount,
        Long reviewCount
) {
    public static ProductLikeReviewDto of(
            Long likeCount,
            Long reviewCount
    ) {
        return new ProductLikeReviewDto(
                likeCount,
                reviewCount
        );
    }
}

