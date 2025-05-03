package com.example.mate.chat.application.dto;

public record ChatLastMessageAndCountDto(
        String message,
        Long messageCount,
        String createdAt
) {
}
