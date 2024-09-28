package pl.owolny.identityprovider.domain.credentials.exception;

import pl.owolny.identityprovider.domain.user.UserId;

public class CredentialsNotFound extends RuntimeException {

    public CredentialsNotFound(UserId userId) {
        super("Credentials not found for user with id: " + userId);
    }
}
