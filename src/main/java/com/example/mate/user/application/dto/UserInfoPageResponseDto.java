package com.example.mate.user.application.dto;

import com.example.mate.product.application.dto.ProductAllResponseDto;

import java.util.List;

public record UserInfoPageResponseDto(
        List<UserInfoResponseDto> content,
        int currentPage,
        boolean hasNext
) {
}
