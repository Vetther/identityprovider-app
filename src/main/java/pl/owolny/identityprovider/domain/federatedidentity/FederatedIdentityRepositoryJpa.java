package pl.owolny.identityprovider.domain.federatedidentity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import pl.owolny.identityprovider.domain.user.UserId;

import java.util.Optional;
import java.util.Set;

interface FederatedIdentityRepositoryJpa extends FederatedIdentityRepository, JpaRepository<FederatedIdentity, FederatedIdentityId> {

    Set<FederatedIdentity> findByUserId(UserId userId);
}
