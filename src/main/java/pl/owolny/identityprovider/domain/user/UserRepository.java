package pl.owolny.identityprovider.domain.user;

import pl.owolny.identityprovider.vo.Email;

import java.util.Optional;

interface UserRepository {

    <S extends User> S save(S user);

    Optional<User> findById(UserId id);

    Optional<User> findByEmail(Email email);

    Optional<User> findByUsername(String username);

}
