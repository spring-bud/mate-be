package com.example.mate.upload.exception;

import com.example.mate.common.exception.BaseException;

public class UploadException extends BaseException {

    public UploadException(UploadExceptionType exceptionType) {
        super(exceptionType);
    }
}
