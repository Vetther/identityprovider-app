package pl.owolny.identityprovider.domain.federatedidentity;

import pl.owolny.identityprovider.vo.IdentityProvider;

import java.util.List;
import java.util.Optional;

interface FederatedIdentityRepository {

    <S extends FederatedIdentity> S save(S FederatedIdentity);

    void delete(FederatedIdentity federatedIdentity);

    List<FederatedIdentity> findAll();

    Optional<FederatedIdentity> findById(FederatedIdentityId id);

    Optional<FederatedIdentity> findByExternalIdAndProvider(String externalId, IdentityProvider provider);
}
