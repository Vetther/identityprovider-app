package pl.owolny.identityprovider.domain.user.exception;

import pl.owolny.identityprovider.vo.Email;
import pl.owolny.identityprovider.domain.user.UserId;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String username) {
        super("User with username " + username + " already exists");
    }

    public UserAlreadyExistsException(Email email) {
        super("User with email " + email + " already exists");
    }

    public UserAlreadyExistsException(UserId userId) {
        super("User with id " + userId + " already exists");
    }
}
