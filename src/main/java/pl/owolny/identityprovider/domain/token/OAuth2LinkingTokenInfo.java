package pl.owolny.identityprovider.domain.token;

import pl.owolny.identityprovider.domain.user.UserId;
import pl.owolny.identityprovider.vo.Email;
import pl.owolny.identityprovider.vo.IdentityProvider;

import java.time.LocalDateTime;

public interface OAuth2LinkingTokenInfo {

    LocalDateTime getCreatedAt();

    UserId getUserId();

    Email getUserEmail();

    String getUserName();

    String getExternalId();

    Email getExternalEmail();

    String getExternalUsername();

    IdentityProvider getIdentityProvider();
}
