package pl.owolny.identityprovider.domain.userprofile;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.owolny.identityprovider.domain.user.UserId;

import java.util.Optional;

interface UserProfileRepositoryJpa extends UserProfileRepository, JpaRepository<UserProfile, UserProfileId> {

    Optional<UserProfile> findByUserId(UserId userId);
}
