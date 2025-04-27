package com.example.mate.product.application.dto;

import com.example.mate.product.domain.Product;
import com.example.mate.product.domain.ProductCategory;
import com.example.mate.product.domain.ProductTag;

import java.util.List;

public record ProductUpdateRequestDto(
        Long id,
        String title,
        ProductCategory category,
        List<ProductTagInfo> tags,
        String content,
        String thumbnailUrl
) {
    public static ProductUpdateRequestDto of(Product product) {
        return new ProductUpdateRequestDto(
                product.getId(),
                product.getTitle(),
                product.getCategory(),
                product.getProductTags().stream()
                        .map(ProductTagInfo::from)
                        .toList(),
                product.getContent(),
                product.getThumbnailUrl()
        );
    }

    public record ProductTagInfo(
            Long stackId,
            String name
    ) {
        public static ProductTagInfo from(ProductTag productTag) {
            return new ProductTagInfo(
                    productTag.getTag().getId(),
                    productTag.getTag().getName()
            );
        }
    }
}
