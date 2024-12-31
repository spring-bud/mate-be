package com.example.mate.product.exception;

import com.example.mate.common.exception.BaseException;

public class ProductException extends BaseException {

    public ProductException(ProductExceptionType exceptionType) {
        super(exceptionType);
    }
}
