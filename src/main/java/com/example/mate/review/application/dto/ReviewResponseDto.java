package com.example.mate.review.application.dto;

import com.example.mate.review.domain.Review;

import java.util.List;
import java.util.stream.Collectors;

public record ReviewResponseDto(
        Long id,
        Double star,
        String content
) {
    public static ReviewResponseDto of(Review review) {
        return new ReviewResponseDto(
                review.getId(),
                review.getStar(),
                review.getContent()
        );
    }

    public static List<ReviewResponseDto> of(List<Review> reviews) {
        return reviews.stream()
                .map(ReviewResponseDto::of)
                .collect(Collectors.toList());
    }
}
