package pl.owolny.identityprovider.infrastructure.authentication.credentials;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.owolny.identityprovider.domain.credentials.CredentialsService;
import pl.owolny.identityprovider.domain.user.UserId;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class CredentialsAuthenticationProviderTest {

    @Mock
    private CredentialsService credentialsService;

    @Mock
    private CredentialsUserService userDetailsService;

    @InjectMocks
    private CredentialsAuthenticationProvider authenticationProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAuthenticateUserSuccessfully() {
        // Arrange
        var usernameOrEmail = "test@example.com";
        var password = "password123";

        var userId = new UserId(UUID.randomUUID());
        var authorities = Set.of(new SimpleGrantedAuthority("USER_READ"));

        var authenticationPrincipal = new CredentialsAuthenticatedUser(userId, authorities);
        when(userDetailsService.loadUserByUsername(usernameOrEmail)).thenReturn(authenticationPrincipal);
        when(credentialsService.checkPassword(userId, password)).thenReturn(true);

        Authentication authentication = new UsernamePasswordAuthenticationToken(usernameOrEmail, password);

        // Act
        Authentication result = authenticationProvider.authenticate(authentication);

        // Assert
        assertEquals(authenticationPrincipal, result.getPrincipal());
        assertEquals(password, result.getCredentials());
    }

    @Test
    void shouldThrowBadCredentialsExceptionWhenPasswordIsInvalid() {
        // Arrange
        var usernameOrEmail = "test@example.com";
        var password = "password123";

        var userId = new UserId(UUID.randomUUID());
        var authorities = Set.of(new SimpleGrantedAuthority("USER_READ"));

        var authenticationPrincipal = new CredentialsAuthenticatedUser(userId, authorities);
        when(userDetailsService.loadUserByUsername(usernameOrEmail)).thenReturn(authenticationPrincipal);
        when(credentialsService.checkPassword(userId, password)).thenReturn(false);

        Authentication authentication = new UsernamePasswordAuthenticationToken(usernameOrEmail, password);

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authenticationProvider.authenticate(authentication));
    }

    @Test
    void shouldThrowBadCredentialsExceptionWhenUserNotFound() {
        // Arrange
        var usernameOrEmail = "unknown@example.com";
        var password = "password123";

        when(userDetailsService.loadUserByUsername(usernameOrEmail)).thenThrow(new UsernameNotFoundException("User not found"));

        Authentication authentication = new UsernamePasswordAuthenticationToken(usernameOrEmail, password);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> authenticationProvider.authenticate(authentication));
    }
}
