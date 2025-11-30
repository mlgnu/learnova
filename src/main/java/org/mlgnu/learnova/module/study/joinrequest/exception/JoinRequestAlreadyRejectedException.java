package org.mlgnu.learnova.module.study.joinrequest.exception;

import org.mlgnu.learnova.common.exception.DomainException;

public class JoinRequestAlreadyRejectedException extends DomainException {
    public JoinRequestAlreadyRejectedException(Long identifier) {
        super("Join request already rejected" + identifier);
    }
}
