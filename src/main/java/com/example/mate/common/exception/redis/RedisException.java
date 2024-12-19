package com.example.mate.common.exception.redis;

import com.example.mate.common.exception.BaseException;
import com.example.mate.common.exception.BaseExceptionType;

public class RedisException extends BaseException {

    public RedisException(BaseExceptionType baseExceptionType) {
        super(baseExceptionType);
    }
}
