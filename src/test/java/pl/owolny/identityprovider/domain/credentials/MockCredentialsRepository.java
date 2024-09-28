package pl.owolny.identityprovider.domain.credentials;

import pl.owolny.identityprovider.common.InMemoryJpaRepository;
import pl.owolny.identityprovider.domain.user.UserId;

import java.util.Optional;

class MockCredentialsRepository extends InMemoryJpaRepository<Credentials, CredentialsId> implements CredentialsRepository {

    MockCredentialsRepository() {
        super(Credentials::getId);
    }

    @Override
    public Optional<Credentials> findByUserId(UserId id) {
        return super.database.values().stream()
                .filter(credentials -> credentials.getUserId().equals(id))
                .findFirst();
    }
}
