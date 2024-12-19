package com.example.mate.auth.exception;

import com.example.mate.common.exception.BaseException;

public class AuthException extends BaseException {
  
    public AuthException(AuthExceptionType authExceptionType) {
        super(authExceptionType);
    }
}
