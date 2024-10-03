package pl.owolny.identityprovider.domain.federatedidentity;

import pl.owolny.identityprovider.vo.Email;
import pl.owolny.identityprovider.vo.IdentityProvider;
import pl.owolny.identityprovider.domain.user.UserId;

import java.util.Optional;

public interface FederatedIdentityService {

    FederatedIdentityInfo createNew(UserId userId, String externalId, IdentityProvider provider, String username, Email email, boolean isEmailVerified);

    Optional<FederatedIdentityInfo> getByExternalId(String externalId, IdentityProvider provider);

    void verifyEmail(FederatedIdentityId federatedIdentityId);
}
