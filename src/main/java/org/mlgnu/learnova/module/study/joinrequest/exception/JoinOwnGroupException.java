package org.mlgnu.learnova.module.study.joinrequest.exception;

import org.mlgnu.learnova.common.exception.DomainException;

public class JoinOwnGroupException extends DomainException {
    public JoinOwnGroupException(Long userId) {
        super("User with id " + userId + " cannot join their own group");
    }
}
