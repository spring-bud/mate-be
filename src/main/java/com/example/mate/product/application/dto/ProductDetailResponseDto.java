package com.example.mate.product.application.dto;

import com.example.mate.product.domain.Product;
import com.example.mate.product.domain.ProductCategory;
import com.example.mate.product.domain.ProductStatus;
import com.example.mate.product.domain.ProductTag;
import com.example.mate.user.domain.User;
import java.util.List;

public record ProductDetailResponseDto(
        String title,
        String content,
        ProductCategory category,
        ProductStatus status,
        List<ProductTagInfo> productTags,
        OwnerInfo owner,
        CountInfo count,
        Boolean isLiked
) {

    public static ProductDetailResponseDto of(
            Product product,
            Long likeCount,
            Long reviewCount,
            Boolean isLiked
    ) {
        return new ProductDetailResponseDto(
                product.getTitle(),
                product.getContent(),
                product.getCategory(),
                product.getStatus(),
                product.getProductTags().stream()
                        .map(ProductTagInfo::from)
                        .toList(),
                OwnerInfo.of(product.getUser()),
                CountInfo.of(likeCount, reviewCount),
                isLiked
        );
    }

    public record ProductTagInfo(
            Long tagId,
            String name
    ) {
        public static ProductTagInfo from(ProductTag productTag) {
            return new ProductTagInfo(
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

        public static OwnerInfo of(User user) {
            return new OwnerInfo(
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

        public static CountInfo of(Long likeCount, Long reviewCount) {
            return new CountInfo(likeCount, reviewCount);
        }
    }
}
