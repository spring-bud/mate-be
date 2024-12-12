package com.example.mate.common.exception;

public record ExceptionResponseDto(
        int status,
        String message
) {
}
