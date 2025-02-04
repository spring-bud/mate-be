package com.example.mate.chat.exception;

import com.example.mate.common.exception.BaseException;

public class ChatException extends BaseException {

    public ChatException(ChatExceptionType chatExceptionType) {
        super(chatExceptionType);
    }
}
