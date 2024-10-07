package pl.owolny.identityprovider.infrastructure.authentication.oauth2;

import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;

public interface OAuth2UserMapper {

    OAuth2UserInfo map(OAuth2LoginAuthenticationToken authenticationToken);
}
