package pl.owolny.identityprovider.infrastructure.authentication.oauth2.exception;

import org.springframework.security.core.AuthenticationException;
import pl.owolny.identityprovider.domain.user.UserInfo;
import pl.owolny.identityprovider.infrastructure.authentication.oauth2.OAuth2UserInfo;

public class OAuth2EmailUnverifiedException extends AuthenticationException {

    public OAuth2EmailUnverifiedException(OAuth2UserInfo oAuth2UserInfo) {
        super("Email verification required");
    }

    public OAuth2EmailUnverifiedException(UserInfo userInfo, OAuth2UserInfo oAuth2UserInfo) {
        super("Email verification required");
    }
}
