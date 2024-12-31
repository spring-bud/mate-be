package com.example.mate.common.response;

public record ApiResponse<T>(
        T data
) {
}
