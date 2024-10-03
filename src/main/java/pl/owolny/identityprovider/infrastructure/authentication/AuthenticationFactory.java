package pl.owolny.identityprovider.infrastructure.authentication;

import org.springframework.security.core.Authentication;

public interface AuthenticationFactory {

    <U extends AuthenticatedUser> Authentication createSuccessAuthentication(U user, Authentication authentication);
}
