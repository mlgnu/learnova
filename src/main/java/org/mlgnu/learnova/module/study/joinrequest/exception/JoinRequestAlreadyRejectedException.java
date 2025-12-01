package org.mlgnu.learnova.module.study.joinrequest.exception;

import org.mlgnu.learnova.common.exception.DomainException;

public class JoinRequestAlreadyRejectedException extends DomainException {
    public JoinRequestAlreadyRejectedException(Object id) {
        super("Join request already rejected: " + id);
    }
}
