package pl.owolny.identityprovider.infrastructure.authentication.oidc;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import pl.owolny.identityprovider.domain.user.UserId;
import pl.owolny.identityprovider.infrastructure.authentication.AuthenticatedUser;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

record OidcAuthenticatedUser(UserId userId,
                             Collection<? extends GrantedAuthority> authorities,
                             Map<String, Object> attributes,
                             Map<String, Object> claims,
                             OidcUserInfo userInfo,
                             OidcIdToken idToken)
        implements AuthenticatedUser, OidcUser {

    @Override
    public UserId getUserId() {
        return this.userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getName() {
        return this.userId.value().toString();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Map<String, Object> getClaims() {
        return this.claims;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return this.userInfo;
    }

    @Override
    public OidcIdToken getIdToken() {
        return this.idToken;
    }
}
