package pl.owolny.identityprovider.domain.federatedidentity;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.owolny.identityprovider.vo.IdentityProvider;

import java.util.List;
import java.util.Optional;
import java.util.Set;

interface FederatedIdentityRepositoryJpa extends FederatedIdentityRepository, JpaRepository<FederatedIdentity, FederatedIdentityId> {

    Optional<FederatedIdentity> findByExternalIdAndProvider(String externalId, IdentityProvider provider);

}
