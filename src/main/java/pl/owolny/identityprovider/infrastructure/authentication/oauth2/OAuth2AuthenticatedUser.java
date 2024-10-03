package pl.owolny.identityprovider.infrastructure.authentication.oauth2;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import pl.owolny.identityprovider.domain.user.UserId;
import pl.owolny.identityprovider.infrastructure.authentication.AuthenticatedUser;

import java.util.Collection;
import java.util.Map;

record OAuth2AuthenticatedUser(UserId userId,
                               Collection<? extends GrantedAuthority> authorities,
                               Map<String, Object> attributes)
        implements AuthenticatedUser, OAuth2User {

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
}
