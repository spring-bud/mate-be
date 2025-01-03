package com.example.mate.proposal.exception;

import com.example.mate.common.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum ProposalExceptionType implements BaseExceptionType {
    NOT_EXIST_Proposal("존재하지 않는 제안서입니다.", HttpStatus.NOT_FOUND),
    ;

    private final String message;
    private final HttpStatus status;

    ProposalExceptionType(String message, HttpStatus status) {
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
