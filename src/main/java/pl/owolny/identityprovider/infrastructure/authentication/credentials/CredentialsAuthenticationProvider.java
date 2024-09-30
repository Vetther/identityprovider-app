package pl.owolny.identityprovider.infrastructure.authentication.credentials;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import pl.owolny.identityprovider.domain.credentials.CredentialsService;

@Slf4j
class CredentialsAuthenticationProvider implements AuthenticationProvider {

    private final CredentialsService credentialsService;
    private final CredentialsUserService credentialsUserService;

    public CredentialsAuthenticationProvider(CredentialsService credentialsService, CredentialsUserService credentialsUserService) {
        this.credentialsService = credentialsService;
        this.credentialsUserService = credentialsUserService;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        var usernameOrEmail = authentication.getName();
        var password = authentication.getCredentials().toString();

        CredentialsAuthenticatedUser authUser = credentialsUserService.loadUserByUsername(usernameOrEmail);

        if (!credentialsService.checkPassword(authUser.userId(), password)) {
            throw new BadCredentialsException("Invalid credentials");
        }

        return createSuccessAuthentication(authUser, authentication);
    }

    protected Authentication createSuccessAuthentication(CredentialsAuthenticatedUser principal, Authentication authentication) {
        UsernamePasswordAuthenticationToken result = UsernamePasswordAuthenticationToken.authenticated(principal, authentication.getCredentials(), principal.authorities());
        result.setDetails(authentication.getDetails());
        log.info("User authenticated: {}", principal);
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
