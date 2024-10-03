package pl.owolny.identityprovider.infrastructure.authentication.oidc;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.oidc.authentication.OidcAuthorizationCodeAuthenticationProvider;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import pl.owolny.identityprovider.domain.user.UserId;

import java.util.UUID;

class OidcAuthenticationProvider extends OidcAuthorizationCodeAuthenticationProvider {

    public OidcAuthenticationProvider() {
        super(new DefaultAuthorizationCodeTokenResponseClient(), new OidcUserService());
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuth2LoginAuthenticationToken authenticated = (OAuth2LoginAuthenticationToken) super.authenticate(authentication);
        OidcUser oidcUser = (OidcUser) authenticated.getPrincipal();

        OidcAuthenticatedUser user = new OidcAuthenticatedUser(
                new UserId(UUID.randomUUID()),
                oidcUser.getAuthorities(),
                oidcUser.getAttributes(),
                oidcUser.getClaims(),
                oidcUser.getUserInfo(),
                oidcUser.getIdToken()
        );

        return createSuccessAuthentication(user, authenticated);
    }

    private OAuth2LoginAuthenticationToken createSuccessAuthentication(OidcAuthenticatedUser principal, OAuth2LoginAuthenticationToken authentication) {
        OAuth2LoginAuthenticationToken authenticationResult = new OAuth2LoginAuthenticationToken(
                authentication.getClientRegistration(),
                authentication.getAuthorizationExchange(),
                principal,
                principal.authorities(),
                authentication.getAccessToken(),
                authentication.getRefreshToken()
        );
        authenticationResult.setDetails(authentication.getDetails());
        return authenticationResult;
    }
}
