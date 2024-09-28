package pl.owolny.identityprovider.domain.federatedidentity;

import pl.owolny.identityprovider.domain.user.UserId;

import java.util.Optional;
import java.util.Set;

interface FederatedIdentityRepository {

    <S extends FederatedIdentity> S save(S FederatedIdentity);

    void delete(FederatedIdentity federatedIdentity);

    Optional<FederatedIdentity> findById(FederatedIdentityId id);

    Set<FederatedIdentity> findByUserId(UserId userId);
}
