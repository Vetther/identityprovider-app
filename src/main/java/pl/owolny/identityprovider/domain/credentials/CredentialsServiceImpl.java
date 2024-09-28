package pl.owolny.identityprovider.domain.credentials;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import pl.owolny.identityprovider.vo.PasswordHash;
import pl.owolny.identityprovider.domain.credentials.exception.CredentialsAlreadyExists;
import pl.owolny.identityprovider.domain.credentials.exception.CredentialsNotFound;
import pl.owolny.identityprovider.domain.user.UserId;

class CredentialsServiceImpl implements CredentialsService {

    private final PasswordEncoder passwordEncoder;
    private final CredentialsRepository credentialsRepository;

    public CredentialsServiceImpl(PasswordEncoder passwordEncoder, CredentialsRepository credentialsRepository) {
        this.passwordEncoder = passwordEncoder;
        this.credentialsRepository = credentialsRepository;
    }

    @Transactional
    @Override
    public void createNew(UserId userId, String rawPassword) {
        if (this.credentialsRepository.findByUserId(userId).isPresent()) {
            throw new CredentialsAlreadyExists(userId);
        }
        String encodedPassword = this.passwordEncoder.encode(rawPassword);
        Credentials credentials = new Credentials(userId, new PasswordHash(encodedPassword));
        this.credentialsRepository.save(credentials);
    }

    @Transactional
    @Override
    public void updatePassword(UserId userId, PasswordHash password) {
        Credentials credentials = this.credentialsRepository.findByUserId(userId)
                .orElseThrow(() -> new CredentialsNotFound(userId));
        credentials.updatePassword(password);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean checkPassword(UserId userId, String rawPassword) {
        return this.credentialsRepository.findByUserId(userId)
                .map(credentials -> this.passwordEncoder.matches(rawPassword, credentials.getPassword().value()))
                .orElse(false);
    }
}
