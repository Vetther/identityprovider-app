package pl.owolny.identityprovider.domain.credentials;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.owolny.identityprovider.vo.PasswordHash;
import pl.owolny.identityprovider.domain.credentials.exception.CredentialsAlreadyExists;
import pl.owolny.identityprovider.domain.credentials.exception.CredentialsNotFound;
import pl.owolny.identityprovider.domain.user.UserId;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CredentialsServiceImplTest {

    private final CredentialsConfig credentialsConfig = new CredentialsConfig(new MockCredentialsRepository());
    private final CredentialsService credentialsService = credentialsConfig.credentialsService();
    private final PasswordEncoder passwordEncoder = credentialsConfig.encoder();

    @Test
    void createNew_ShouldCreateAndSaveCredentials() {
        UserId userId = UserId.of(UUID.randomUUID());
        String rawPassword = "password123";

        credentialsService.createNew(userId, rawPassword);

        assertTrue(credentialsService.checkPassword(userId, rawPassword));
    }

    @Test
    void createNew_ShouldThrowExceptionWhenCredentialsAlreadyExist() {
        UserId userId = UserId.of(UUID.randomUUID());
        String rawPassword = "password123";

        credentialsService.createNew(userId, rawPassword);

        assertThrows(CredentialsAlreadyExists.class, () -> credentialsService.createNew(userId, rawPassword));
    }

    @Test
    void updatePassword_ShouldUpdateExistingCredentials() {
        UserId userId = UserId.of(UUID.randomUUID());
        String rawPassword = "password123";
        credentialsService.createNew(userId, rawPassword);

        PasswordHash newPassword = new PasswordHash(passwordEncoder.encode("newPassword123"));
        credentialsService.updatePassword(userId, newPassword);

        assertTrue(credentialsService.checkPassword(userId, "newPassword123"));
    }

    @Test
    void updatePassword_ShouldThrowExceptionWhenCredentialsDoNotExist() {
        UserId userId = UserId.of(UUID.randomUUID());
        PasswordHash newPassword = new PasswordHash(passwordEncoder.encode("newPassword123"));

        assertThrows(CredentialsNotFound.class, () -> credentialsService.updatePassword(userId, newPassword));
    }

    @Test
    void checkPassword_ShouldReturnTrueForCorrectPassword() {
        UserId userId = UserId.of(UUID.randomUUID());
        String rawPassword = "password123";
        credentialsService.createNew(userId, rawPassword);

        assertTrue(credentialsService.checkPassword(userId, rawPassword));
    }

    @Test
    void checkPassword_ShouldReturnFalseForIncorrectPassword() {
        UserId userId = UserId.of(UUID.randomUUID());
        String rawPassword = "password123";
        credentialsService.createNew(userId, rawPassword);

        assertFalse(credentialsService.checkPassword(userId, "wrongPassword123"));
    }

    @Test
    void checkPassword_ShouldReturnFalseWhenCredentialsDoNotExist() {
        UserId userId = UserId.of(UUID.randomUUID());
        String rawPassword = "password123";

        assertFalse(credentialsService.checkPassword(userId, rawPassword));
    }
}