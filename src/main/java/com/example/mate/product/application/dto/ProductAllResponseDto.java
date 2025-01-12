package com.example.mate.product.application.dto;

import com.example.mate.product.domain.Product;
import com.example.mate.product.domain.ProductCategory;
import com.example.mate.product.domain.ProductStatus;
import com.example.mate.product.domain.ProductTag;
import com.example.mate.user.domain.User;

import java.util.List;

public record ProductAllResponseDto(
        String title,
        String content,
        ProductCategory category,
        ProductStatus status,
        List<ProductAllResponseDto.ProductTagInfo> productTags,
        ProductAllResponseDto.OwnerInfo owner,
        ProductAllResponseDto.CountInfo count,
        Boolean isLiked
) {

    public static ProductAllResponseDto of(
            Product product,
            Long likeCount,
            Long reviewCount,
            Boolean isLiked
    ) {
        return new ProductAllResponseDto(
                product.getTitle(),
                product.getContent(),
                product.getCategory(),
                product.getStatus(),
                product.getProductTags().stream()
                        .map(ProductAllResponseDto.ProductTagInfo::from)
                        .toList(),
                ProductAllResponseDto.OwnerInfo.of(product.getUser()),
                ProductAllResponseDto.CountInfo.of(likeCount, reviewCount),
                isLiked
        );
    }

    public record ProductTagInfo(
            Long tagId,
            String name
    ) {
        public static ProductAllResponseDto.ProductTagInfo from(ProductTag productTag) {
            return new ProductAllResponseDto.ProductTagInfo(
                    productTag.getTag().getId(),
                    productTag.getTag().getName()
            );
        }
    }

    public record OwnerInfo(
            Long userId,
            String nickname,
            String profileUrl
    ) {

        public static ProductAllResponseDto.OwnerInfo of(User user) {
            return new ProductAllResponseDto.OwnerInfo(
                    user.getId(),
                    user.getNickname(),
                    user.getProfileUrl()
            );
        }
    }

    public record CountInfo(
            Long likeCount,
            Long reviewCount
    ) {

        public static ProductAllResponseDto.CountInfo of(Long likeCount, Long reviewCount) {
            return new ProductAllResponseDto.CountInfo(likeCount, reviewCount);
        }
    }
}
