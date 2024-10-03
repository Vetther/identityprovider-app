package pl.owolny.identityprovider.domain.user;

import pl.owolny.identityprovider.vo.Email;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    UserInfo createNew(String username, Email email, boolean isEmailVerified, boolean isActive);

    UserInfo getById(UserId userId);

    UserInfo getByUsername(String username);

    UserInfo getByEmail(Email email);

    Optional<UserInfo> findByEmail(Email email);

    void verifyEmail(UserId id);

    void activate(UserId id);

    boolean existsByUsername(String username);

    boolean existsByEmail(Email email);

    boolean existsById(UUID id);
}
