package org.mlgnu.learnova.module.study.joinrequest.exception;

import org.mlgnu.learnova.common.exception.DomainException;

public class JoinRequestAlreadyApprovedException extends DomainException {
    public JoinRequestAlreadyApprovedException(Long identifier) {
        super("Join request already approved" + identifier);
    }
}
