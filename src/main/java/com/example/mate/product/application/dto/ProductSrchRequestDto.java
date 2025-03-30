package com.example.mate.product.application.dto;

import java.util.List;

public record ProductSrchRequestDto(
        String category,
        String sort,
        List<String> tag,
        String title
) {
}
