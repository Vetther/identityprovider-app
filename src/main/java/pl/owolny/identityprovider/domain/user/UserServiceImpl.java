package pl.owolny.identityprovider.domain.user;

import org.springframework.transaction.annotation.Transactional;
import pl.owolny.identityprovider.vo.Email;
import pl.owolny.identityprovider.domain.user.exception.UserAlreadyExistsException;
import pl.owolny.identityprovider.domain.user.exception.UserNotFoundException;

import java.util.Optional;
import java.util.UUID;

class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserInfo createNew(String username, Email email, boolean isEmailVerified, boolean isActive) {
        var user = new User(username, email, isEmailVerified, isActive);
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException(email);
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void verifyEmail(UserId userId) {
        User user = (User) getById(userId);
        user.verifyEmail();
    }

    @Override
    @Transactional
    public void activate(UserId userId) {
        User user = (User) getById(userId);
        user.activateUser();
    }

    @Override
    @Transactional(readOnly = true)
    public UserInfo getById(UserId userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public UserInfo getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    @Transactional(readOnly = true)
    public UserInfo getByEmail(Email email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    @Override
    public Optional<UserInfo> findByEmail(Email email) {
        return userRepository.findByEmail(email).map(user -> user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(Email email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return userRepository.findById(UserId.of(id)).isPresent();
    }
}
