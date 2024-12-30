package com.example.mate.product.application.dto;

import com.example.mate.product.domain.Product;

public record ProductIdResponseDto(
        Long productId
) {

    public static ProductIdResponseDto of(Product product) {
        return new ProductIdResponseDto(product.getId());
    }
}
