package com.example.mate.product.application.dto;

import java.util.List;

public record ProductLikeSrchResponseDto(
        List<ProductAllResponseDto> content
) {
}
