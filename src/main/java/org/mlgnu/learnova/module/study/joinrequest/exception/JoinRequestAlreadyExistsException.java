package org.mlgnu.learnova.module.study.joinrequest.exception;

import org.mlgnu.learnova.common.exception.DomainException;

public class JoinRequestAlreadyExistsException extends DomainException {

    public JoinRequestAlreadyExistsException(String userId) {
        super("Join request already exist for user " + userId);
    }
}
