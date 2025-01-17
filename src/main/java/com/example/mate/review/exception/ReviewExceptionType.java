package com.example.mate.review.exception;

import com.example.mate.common.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum ReviewExceptionType implements BaseExceptionType {

    NOT_EXIST_REVIEW("존재하지 않는 리뷰입니다.", HttpStatus.NOT_FOUND),
    NO_PERMISSIONS_ON_REVIEW(" 리뷰에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN),
    ;

    private final String message;
    private final HttpStatus status;

    ReviewExceptionType(String message, HttpStatus status) {
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
