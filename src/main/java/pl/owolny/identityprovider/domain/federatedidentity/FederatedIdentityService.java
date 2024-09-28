package pl.owolny.identityprovider.domain.federatedidentity;

import org.springframework.stereotype.Repository;
import pl.owolny.identityprovider.vo.Email;
import pl.owolny.identityprovider.vo.IdentityProvider;
import pl.owolny.identityprovider.domain.user.UserId;

import java.util.Set;

@Repository
public interface FederatedIdentityService {

    FederatedIdentity createNew(UserId userId, String externalId, IdentityProvider provider, String username, Email email, boolean isEmailVerified);

    Set<FederatedIdentity> getByUserId(UserId userId);

    void verifyEmail(FederatedIdentityId federatedIdentityId);
}
