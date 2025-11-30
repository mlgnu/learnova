package org.mlgnu.learnova.module.study.joinrequest.exception;

import org.mlgnu.learnova.common.exception.DomainException;

public class JoinRequestCancelledException extends DomainException {
    public JoinRequestCancelledException(Long identifier) {
        super("join request is cancelled: " + identifier);
    }
}
