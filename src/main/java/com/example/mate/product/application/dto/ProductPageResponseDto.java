package com.example.mate.product.application.dto;

import java.util.List;

public record ProductPageResponseDto(
        List<ProductAllResponseDto> content,
        int currentPage,
        boolean hasNext
) {
}
