package com.example.mate.common.response;

public record ApiResponse<T>(
        String message,
        T data
) {
}
