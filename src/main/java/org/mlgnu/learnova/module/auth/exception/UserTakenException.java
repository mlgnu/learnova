package org.mlgnu.learnova.module.auth.exception;

public class UserTakenException extends RuntimeException {

    public UserTakenException(String identifier) {
        super("User is taken: " + identifier);
    }

    public static UserTakenException byUsername(String username) {
        return new UserTakenException("username=" + username);
    }

    public static UserTakenException byEmail(String email) {
        return new UserTakenException("email=" + email);
    }
}