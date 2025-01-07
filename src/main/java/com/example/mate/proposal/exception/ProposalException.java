package com.example.mate.proposal.exception;

import com.example.mate.common.exception.BaseException;

public class ProposalException extends BaseException {

    public ProposalException(ProposalExceptionType exceptionType) {
        super(exceptionType);
    }
}
