package pl.owolny.identityprovider.domain.user;

import pl.owolny.identityprovider.common.InMemoryJpaRepository;
import pl.owolny.identityprovider.vo.Email;

import java.util.Optional;

class MockUserRepository extends InMemoryJpaRepository<User, UserId> implements UserRepository {

    MockUserRepository() {
        super(User::getId);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return super.database.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return super.database.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }
}
