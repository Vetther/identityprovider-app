package pl.owolny.identityprovider.infrastructure.authentication;

import org.springframework.security.core.GrantedAuthority;
import pl.owolny.identityprovider.domain.user.UserId;

import java.util.Collection;

public record AuthenticatedUser(
        UserId userId,
        Collection<? extends GrantedAuthority> authorities) {
}
