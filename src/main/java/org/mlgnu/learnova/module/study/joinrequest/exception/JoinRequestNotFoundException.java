package org.mlgnu.learnova.module.study.joinrequest.exception;

public class JoinRequestNotFoundException extends RuntimeException {
    public JoinRequestNotFoundException(Long userId, Long postId) {
        super("Join request not found for user " + userId + " and post " + postId);
    }
}
