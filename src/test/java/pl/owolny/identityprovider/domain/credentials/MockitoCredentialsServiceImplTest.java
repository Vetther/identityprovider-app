package pl.owolny.identityprovider.domain.credentials;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.owolny.identityprovider.vo.PasswordHash;
import pl.owolny.identityprovider.domain.user.UserId;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MockitoCredentialsServiceImplTest {

    @Mock
    private CredentialsRepository credentialsRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CredentialsServiceImpl credentialsService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createNew_ShouldCreateAndSaveCredentials() {
        UserId userId = UserId.of(UUID.randomUUID());
        String rawPassword = "password123";
        String encodedPassword = "encodedPassword123";

        when(credentialsRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        credentialsService.createNew(userId, rawPassword);

        verify(credentialsRepository).save(any(Credentials.class));
        verify(passwordEncoder).encode(rawPassword);
    }

    @Test
    void createNew_ShouldThrowExceptionWhenCredentialsAlreadyExist() {
        UserId userId = UserId.of(UUID.randomUUID());
        String rawPassword = "password123";

        when(credentialsRepository.findByUserId(userId)).thenReturn(Optional.of(mock(Credentials.class)));

        assertThrows(IllegalArgumentException.class, () -> credentialsService.createNew(userId, rawPassword));

        verify(credentialsRepository, never()).save(any(Credentials.class));
    }

    @Test
    void updatePassword_ShouldUpdateExistingCredentials() {
        UserId userId = UserId.of(UUID.randomUUID());
        String rawPassword = "password123";
        PasswordHash newPassword = new PasswordHash("encodedNewPassword");

        Credentials existingCredentials = mock(Credentials.class);
        when(credentialsRepository.findByUserId(userId)).thenReturn(Optional.of(existingCredentials));

        credentialsService.updatePassword(userId, newPassword);

        verify(existingCredentials).updatePassword(newPassword);
        verify(credentialsRepository).save(existingCredentials);
    }

    @Test
    void updatePassword_ShouldThrowExceptionWhenCredentialsDoNotExist() {
        UserId userId = UserId.of(UUID.randomUUID());
        PasswordHash newPassword = new PasswordHash("encodedNewPassword");

        when(credentialsRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> credentialsService.updatePassword(userId, newPassword));

        verify(credentialsRepository, never()).save(any(Credentials.class));
    }

    @Test
    void checkPassword_ShouldReturnTrueForCorrectPassword() {
        UserId userId = UserId.of(UUID.randomUUID());
        String rawPassword = "password123";
        Credentials credentials = mock(Credentials.class);

        when(credentialsRepository.findByUserId(userId)).thenReturn(Optional.of(credentials));
        when(credentials.getPassword()).thenReturn(new PasswordHash("encodedPassword123"));
        when(passwordEncoder.matches(rawPassword, "encodedPassword123")).thenReturn(true);

        assertTrue(credentialsService.checkPassword(userId, rawPassword));

        verify(passwordEncoder).matches(rawPassword, "encodedPassword123");
    }

    @Test
    void checkPassword_ShouldReturnFalseForIncorrectPassword() {
        UserId userId = UserId.of(UUID.randomUUID());
        String rawPassword = "password123";
        Credentials credentials = mock(Credentials.class);

        when(credentialsRepository.findByUserId(userId)).thenReturn(Optional.of(credentials));
        when(credentials.getPassword()).thenReturn(new PasswordHash("encodedPassword123"));

        assertFalse(credentialsService.checkPassword(userId, "wrongPassword123"));

        verify(passwordEncoder).matches("wrongPassword123", "encodedPassword123");
    }

    @Test
    void checkPassword_ShouldReturnFalseWhenCredentialsDoNotExist() {
        UserId userId = UserId.of(UUID.randomUUID());

        when(credentialsRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertFalse(credentialsService.checkPassword(userId, "password123"));

        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }
}