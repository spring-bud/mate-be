package com.example.mate.common.exception;

import org.springframework.http.HttpStatus;

public interface BaseExceptionType {

    String message();

    HttpStatus status();
}

