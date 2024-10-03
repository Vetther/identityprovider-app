package pl.owolny.identityprovider.domain.federatedidentity;

import pl.owolny.identityprovider.domain.user.UserId;
import pl.owolny.identityprovider.vo.Email;
import pl.owolny.identityprovider.vo.IdentityProvider;

import java.time.LocalDateTime;

public interface FederatedIdentityInfo {

    UserId getUserId();

    IdentityProvider getProvider();

    String getExternalId();

    String getExternalUsername();

    Email getExternalEmail();

    boolean isExternalEmailVerified();

    LocalDateTime getConnectedAt();
}
