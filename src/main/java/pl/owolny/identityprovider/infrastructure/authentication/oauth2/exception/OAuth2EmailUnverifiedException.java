package pl.owolny.identityprovider.infrastructure.authentication.oauth2.exception;

import org.springframework.security.core.AuthenticationException;
import pl.owolny.identityprovider.domain.user.UserInfo;
import pl.owolny.identityprovider.infrastructure.authentication.oauth2.OAuth2UserInfo;

public class OAuth2EmailUnverifiedException extends AuthenticationException {

    private final OAuth2UserInfo oAuth2UserInfo;
    private final UserInfo userInfo;

    public OAuth2EmailUnverifiedException(UserInfo userInfo, OAuth2UserInfo oAuth2UserInfo) {
        super(oAuth2UserInfo.provider().name().toLowerCase() + " account is not verified");
        this.oAuth2UserInfo = oAuth2UserInfo;
        this.userInfo = userInfo;
    }

    public OAuth2UserInfo getoAuth2UserInfo() {
        return oAuth2UserInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
}
