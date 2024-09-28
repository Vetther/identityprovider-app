package pl.owolny.identityprovider.domain.credentials;

import pl.owolny.identityprovider.domain.user.UserId;

import java.util.Optional;

interface CredentialsRepository {

    <S extends Credentials> S save(S credentials);

    Optional<Credentials> findByUserId(UserId id);
}
