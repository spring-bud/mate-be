package com.example.mate.user.exception;


import com.example.mate.common.exception.BaseException;

public class UserException extends BaseException {

    public UserException(UserExceptionType exceptionType) {
        super(exceptionType);
    }
}
