package pl.owolny.identityprovider.infrastructure.authentication.oauth2.map;

import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import pl.owolny.identityprovider.infrastructure.authentication.oauth2.OAuth2UserInfo;
import pl.owolny.identityprovider.vo.Email;
import pl.owolny.identityprovider.vo.IdentityProvider;

@Component("google")
class GoogleUserMapper implements OAuth2UserMapper {

    @Override
    public OAuth2UserInfo map(OAuth2LoginAuthenticationToken authenticationToken) {
        OidcUser oidcUser = (OidcUser) authenticationToken.getPrincipal();

        IdentityProvider identityProvider = IdentityProvider.GOOGLE;
        String externalId = oidcUser.getName();
        String externalUsername = oidcUser.getGivenName();
        String externalAvatarUrl = oidcUser.getPicture();
        String externalEmail = oidcUser.getEmail();
        boolean isExternalEmailVerified = oidcUser.getEmailVerified();
        String firstName = oidcUser.getGivenName();
        String lastName = oidcUser.getFamilyName();

        return new OAuth2UserInfo(
                identityProvider,
                externalId,
                externalUsername,
                new Email(externalEmail),
                isExternalEmailVerified,
                externalAvatarUrl,
                firstName,
                lastName,
                null,
                null,
                null,
                null
        );

    }
}
