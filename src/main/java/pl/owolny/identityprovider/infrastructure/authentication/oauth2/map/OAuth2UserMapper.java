package pl.owolny.identityprovider.infrastructure.authentication.oauth2.map;

import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import pl.owolny.identityprovider.infrastructure.authentication.oauth2.OAuth2UserInfo;

public interface OAuth2UserMapper {

    OAuth2UserInfo map(OAuth2LoginAuthenticationToken authenticationToken);
}
