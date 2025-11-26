package org.mlgnu.learnova.module.user.repository;

import org.mlgnu.learnova.module.auth.exception.UserTakenException;
import org.mlgnu.learnova.module.user.model.entity.UserAccount;
import org.mlgnu.learnova.module.user.exception.UserNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findUserAccountByUsername(String username);
    boolean existsUserAccountByEmail(String email);
    boolean existsUserAccountByUsername(String username);

    default UserAccount findUserAccountByUsernameOrThrow(String username) {
        return findUserAccountByUsername(username)
                .orElseThrow(() -> UserNotFoundException.byUsername(username));
    }

    default void uniqueUserAccountByEmailOrThrow(String email) {
        if (existsUserAccountByEmail(email)) {
            throw UserTakenException.byEmail(email);
        }
    }

    default void uniqueUserAccountByUsernameOrThrow(String username) {
        if (existsUserAccountByUsername(username)) {
            throw UserTakenException.byUsername(username);
        }
    }

}
