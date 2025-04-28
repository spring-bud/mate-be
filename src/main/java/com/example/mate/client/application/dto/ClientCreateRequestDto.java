package com.example.mate.client.application.dto;

import com.example.mate.client.domain.ClientCategory;

import java.util.List;

public record ClientCreateRequestDto(
        String title,
        ClientCategory category,
        List<String> ctags,
        String content,
        String thumbnailUrl
) {
}
