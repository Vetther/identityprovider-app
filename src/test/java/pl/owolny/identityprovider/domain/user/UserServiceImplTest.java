package pl.owolny.identityprovider.domain.user;

import org.junit.jupiter.api.Test;
import pl.owolny.identityprovider.vo.Email;
import pl.owolny.identityprovider.domain.user.exception.UserAlreadyExistsException;
import pl.owolny.identityprovider.domain.user.exception.UserNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    private final UserConfig userConfig = new UserConfig(new MockUserRepository());
    private final UserService userService = userConfig.userService();

    @Test
    void createNew_ShouldCreateAndSaveUser() {
        User user = new User("test", Email.of("test@email.com"), false, true);
        User createdUser = (User) userService.createNew(user.getUsername(), user.getEmail(), user.isEmailVerified(), user.isActive());
        assertEquals(createdUser.getUsername(), user.getUsername());
        assertFalse(createdUser.isEmailVerified());
        assertTrue(createdUser.isActive());
    }

    @Test
    void createNew_ShouldThrowExceptionWhenUserWithSameEmailAlreadyExists() {
        User user = new User("test", Email.of("test@email.com"), false, true);
        userService.createNew(user.getUsername(), user.getEmail(), user.isEmailVerified(), user.isActive());
        assertThrows(UserAlreadyExistsException.class, () ->
                userService.createNew(user.getUsername(), user.getEmail(), user.isEmailVerified(), user.isActive())
        );
    }

    @Test
    void getById_ShouldReturnUser() {
        User user = new User("test", Email.of("test@email.com"), true, true);
        User createdUser = (User) userService.createNew(user.getUsername(), user.getEmail(), user.isEmailVerified(), user.isActive());
        UserInfo userInfoById = userService.getById(createdUser.getId());
        assertEquals(userInfoById.getUsername(), user.getUsername());
    }

    @Test
    void getByUsername_ShouldReturnUser() {
        User user = new User("test", Email.of("test@email.com"), true, true);
        userService.createNew(user.getUsername(), user.getEmail(), user.isEmailVerified(), user.isActive());
        UserInfo userInfo = userService.getByUsername(user.getUsername());
        assertEquals(userInfo.getUsername(), user.getUsername());
    }

    @Test
    void getByUsername_ShouldThrowExceptionWhenUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.getByUsername("nonexistent"));
    }

    @Test
    void getByEmail_ShouldReturnUser() {
        User user = new User("test", Email.of("test@email.com"), true, true);
        userService.createNew(user.getUsername(), user.getEmail(), user.isEmailVerified(), user.isActive());
        UserInfo userInfo = userService.getByEmail(user.getEmail());
        assertEquals(userInfo.getUsername(), user.getUsername());
    }

    @Test
    void getByEmail_ShouldThrowExceptionWhenUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.getByEmail(new Email("nonexistent@email.com")));
    }

    @Test
    void verifyEmail_ShouldVerifyEmail() {
        User user = new User("test", Email.of("test@email.com"), false, true);
        User createdUser = (User) userService.createNew(user.getUsername(), user.getEmail(), user.isEmailVerified(), user.isActive());
        userService.verifyEmail(createdUser.getId());
        assertTrue(createdUser.isEmailVerified());
    }

    @Test
    void activate_ShouldActivateUser() {
        User user = new User("test", Email.of("test@email.com"), true, false);
        User createdUser = (User) userService.createNew(user.getUsername(), user.getEmail(), user.isEmailVerified(), user.isActive());
        userService.activate(createdUser.getId());
        assertTrue(createdUser.isActive());
    }
}