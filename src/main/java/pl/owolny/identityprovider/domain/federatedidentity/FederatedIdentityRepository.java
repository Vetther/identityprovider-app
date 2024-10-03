package pl.owolny.identityprovider.domain.federatedidentity;

import pl.owolny.identityprovider.vo.IdentityProvider;

import java.util.Optional;

interface FederatedIdentityRepository {

    <S extends FederatedIdentity> S save(S FederatedIdentity);

    void delete(FederatedIdentity federatedIdentity);

    Optional<FederatedIdentity> findById(FederatedIdentityId id);

    Optional<FederatedIdentity> findByExternalIdAndProvider(String externalId, IdentityProvider provider);
}
