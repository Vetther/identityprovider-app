package pl.owolny.identityprovider.infrastructure.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import pl.owolny.identityprovider.domain.credentials.CredentialsService;

@Slf4j
class AuthenticationProviderImpl implements AuthenticationProvider {

    private final CredentialsService credentialsService;
    private final UserDetailsService userDetailsService;

    public AuthenticationProviderImpl(CredentialsService credentialsService, UserDetailsService userDetailsService) {
        this.credentialsService = credentialsService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        var usernameOrEmail = authentication.getName();
        var password = authentication.getCredentials().toString();

        AuthenticatedUser authUser = userDetailsService.loadUser(usernameOrEmail);

        if (!credentialsService.checkPassword(authUser.userId(), password)) {
            throw new BadCredentialsException("Invalid credentials");
        }

        return createSuccessAuthentication(authUser, authentication);
    }

    protected Authentication createSuccessAuthentication(AuthenticatedUser principal, Authentication authentication) {
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
