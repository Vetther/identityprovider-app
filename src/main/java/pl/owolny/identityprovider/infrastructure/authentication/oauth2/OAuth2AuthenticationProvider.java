package pl.owolny.identityprovider.infrastructure.authentication.oauth2;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationProvider;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import pl.owolny.identityprovider.domain.user.UserId;

import java.util.UUID;

class OAuth2AuthenticationProvider extends OAuth2LoginAuthenticationProvider {

    public OAuth2AuthenticationProvider() {
        super(new DefaultAuthorizationCodeTokenResponseClient(), new DefaultOAuth2UserService());
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuth2LoginAuthenticationToken authenticated = (OAuth2LoginAuthenticationToken) super.authenticate(authentication);
        OAuth2User oAuth2User = authenticated.getPrincipal();

        OAuth2AuthenticatedUser user = new OAuth2AuthenticatedUser(
                new UserId(UUID.randomUUID()),
                oAuth2User.getAuthorities()
        );

        return createSuccessAuthentication(user, authenticated);
    }

    private OAuth2LoginAuthenticationToken createSuccessAuthentication(OAuth2AuthenticatedUser principal, OAuth2LoginAuthenticationToken authentication) {
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

    public boolean supports(Class<?> authentication) {
        return OAuth2LoginAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
