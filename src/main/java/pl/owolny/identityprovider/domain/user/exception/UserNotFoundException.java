package pl.owolny.identityprovider.domain.user.exception;

import pl.owolny.identityprovider.vo.Email;
import pl.owolny.identityprovider.domain.user.UserId;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String username) {
        super("User with username " + username + " not found");
    }

    public UserNotFoundException(Email email) {
        super("User with email " + email + " not found");
    }

    public UserNotFoundException(UserId userId) {
        super("User with id " + userId + " not found");
    }

    public UserNotFoundException(String username, Email email) {
        super("User with username " + username + " or email " + email + " not found");
    }
}
