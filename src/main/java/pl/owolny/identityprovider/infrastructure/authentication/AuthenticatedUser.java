package pl.owolny.identityprovider.infrastructure.authentication;

import org.springframework.security.core.GrantedAuthority;
import pl.owolny.identityprovider.domain.user.UserId;

import java.util.Collection;

public interface AuthenticatedUser {

    UserId getUserId();
    Collection<? extends GrantedAuthority> getAuthorities();
}
