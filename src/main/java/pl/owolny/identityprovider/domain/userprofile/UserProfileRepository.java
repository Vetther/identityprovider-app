package pl.owolny.identityprovider.domain.userprofile;

import pl.owolny.identityprovider.domain.user.UserId;

import java.util.Optional;

interface UserProfileRepository {

    <S extends UserProfile> S save(S UserProfile);

    Optional<UserProfile> findById(UserProfileId id);

    Optional<UserProfile> findByUserId(UserId userId);

}
