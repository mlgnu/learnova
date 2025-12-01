package org.mlgnu.learnova.module.study.joinrequest.exception;

import org.mlgnu.learnova.common.exception.DomainException;

public class JoinRequestNotPendingException extends DomainException {
    public JoinRequestNotPendingException(Object id) {
        super("Join request is not pending: " + id);
    }
}
