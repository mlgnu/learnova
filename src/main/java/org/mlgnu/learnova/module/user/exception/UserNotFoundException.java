package org.mlgnu.learnova.module.user.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String identifier) {
        super("User not found: " + identifier);
    }

    public static UserNotFoundException byUsername(String username) {
        return new UserNotFoundException("username=" + username);
    }

    public static UserNotFoundException byId(Long id) {
        return new UserNotFoundException("id=" + id);
    }
}
