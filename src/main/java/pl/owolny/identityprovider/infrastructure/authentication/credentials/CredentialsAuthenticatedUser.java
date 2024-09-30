package pl.owolny.identityprovider.infrastructure.authentication.credentials;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.owolny.identityprovider.domain.user.UserId;
import pl.owolny.identityprovider.infrastructure.authentication.AuthenticatedUser;

import java.util.Collection;

record CredentialsAuthenticatedUser(UserId userId,Collection<? extends GrantedAuthority> authorities)
        implements AuthenticatedUser, UserDetails {

    @Override
    public UserId getUserId() {
        return this.userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.userId.value().toString();
    }
}
