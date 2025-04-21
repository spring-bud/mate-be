package com.example.mate.product.application.dto;

import com.example.mate.product.domain.Product;
import com.example.mate.product.domain.ProductCategory;

import java.time.LocalDateTime;

public record ProductUserResponseDto(
        Long id,
        String title,
        String thumbnailUrl,
        ProductCategory category,
        LocalDateTime createdAt,
        ProductUserResponseDto.CountInfo count
) {

    public static ProductUserResponseDto of(
            Product product,
            Long likeCount,
            Long reviewCount
    ) {
        return new ProductUserResponseDto(
                product.getId(),
                product.getTitle(),
                product.getThumbnailUrl(),
                product.getCategory(),
                product.getCreatedAt(),
                ProductUserResponseDto.CountInfo.of(likeCount, reviewCount)
        );
    }

    public record CountInfo(
            Long likeCount,
            Long reviewCount
    ) {

        public static ProductUserResponseDto.CountInfo of(Long likeCount, Long reviewCount) {
            return new ProductUserResponseDto.CountInfo(likeCount, reviewCount);
        }
    }
}
