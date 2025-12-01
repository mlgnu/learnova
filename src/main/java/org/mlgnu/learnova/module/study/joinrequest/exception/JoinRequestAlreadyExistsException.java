package org.mlgnu.learnova.module.study.joinrequest.exception;

import org.mlgnu.learnova.common.exception.DomainException;

public class JoinRequestAlreadyExistsException extends DomainException {
    public JoinRequestAlreadyExistsException(Object id) {
        super("Join request already exists for user " + id);
    }
}
