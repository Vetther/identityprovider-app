package pl.owolny.identityprovider.infrastructure.authentication;

import org.springframework.security.core.Authentication;

public interface AuthenticationFactory {

    Authentication createSuccessAuthentication(AuthenticatedUser user, Authentication authentication);
}
