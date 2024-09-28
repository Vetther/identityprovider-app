package pl.owolny.identityprovider.domain.credentials;

import pl.owolny.identityprovider.vo.PasswordHash;
import pl.owolny.identityprovider.domain.user.UserId;

public interface CredentialsService {

    void createNew(UserId userId, String rawPassword);

    void updatePassword(UserId userId, PasswordHash password);

    boolean checkPassword(UserId userId, String rawPassword);
}
