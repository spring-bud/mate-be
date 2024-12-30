package com.example.mate.product.exception;

import com.example.mate.common.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum ProductExceptionType implements BaseExceptionType {
    PRODUCT_NOT_FOUND_EXCEPTION("존재하지 않는 프로덕트입니다.", HttpStatus.NOT_FOUND),
    NO_PERMISSIONS_ON_PRODUCT("프로덕트에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN),
    ;

    private final String message;
    private final HttpStatus status;

    ProductExceptionType(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public HttpStatus status() {
        return status;
    }
}
