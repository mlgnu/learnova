package org.mlgnu.learnova.module.study.joinrequest.exception;

import org.mlgnu.learnova.common.exception.DomainException;

public class JoinRequestCancelledException extends DomainException {
    public JoinRequestCancelledException(Long identifier) {
        super("Join request is cancelled: " + identifier);
    }
}
