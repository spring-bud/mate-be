package com.example.mate.review.exception;

import com.example.mate.common.exception.BaseException;

public class ReviewException extends BaseException {
    public ReviewException(ReviewExceptionType exceptionType) {
        super(exceptionType);
    }
}
