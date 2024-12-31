package com.example.mate.product.application.dto;

import com.example.mate.product.domain.ProductCategory;
import java.util.List;

public record ProductCreateRequestDto(
        String title,
        ProductCategory category,
        List<String> tags,
        String content,
        String thumbnailUrl
) {
}
