package pl.owolny.identityprovider.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.owolny.identityprovider.vo.Email;

import java.util.Optional;

interface UserRepositoryJpa extends UserRepository, JpaRepository<User, UserId> {

    Optional<User> findByEmail(Email email);

    default Optional<User> findByUsername(String username) {
        return findByUsernameIgnoreCase(username);
    }

    Optional<User> findByUsernameIgnoreCase(String username);
}
