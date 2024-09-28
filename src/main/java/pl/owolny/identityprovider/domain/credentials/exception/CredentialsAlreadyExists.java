package pl.owolny.identityprovider.domain.credentials.exception;

import pl.owolny.identityprovider.domain.user.UserId;

public class CredentialsAlreadyExists extends RuntimeException {

    public CredentialsAlreadyExists(UserId userId) {
        super("Credentials already exists for user with id: " + userId);
    }
}
