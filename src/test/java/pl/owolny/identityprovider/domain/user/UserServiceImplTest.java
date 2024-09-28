package pl.owolny.identityprovider.domain.user;

import org.junit.jupiter.api.Test;
import pl.owolny.identityprovider.vo.Email;
import pl.owolny.identityprovider.domain.user.exception.UserAlreadyExistsException;
import pl.owolny.identityprovider.domain.user.exception.UserNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    private final UserConfiguration userConfiguration = new UserConfiguration(new MockUserRepository());
    private final UserService userService = userConfiguration.userService();

    @Test
    void createNew_ShouldCreateAndSaveUser() {
        User user = new User("test", Email.of("test@email.com"));
        UserInfo userInfo = userService.createNew(user.getUsername(), user.getEmail());
        assertEquals(userInfo.getUsername(), user.getUsername());
    }

    @Test
    void createNew_ShouldThrowExceptionWhenUserWithSameEmailAlreadyExists() {
        User user = new User("test", Email.of("test@email.com"));
        userService.createNew(user.getUsername(), user.getEmail());
        assertThrows(UserAlreadyExistsException.class, () -> userService.createNew(user.getUsername(), user.getEmail()));
    }

    @Test
    void getById_ShouldReturnUser() {
        User user = new User("test", Email.of("test@email.com"));
        User createdUser = (User) userService.createNew(user.getUsername(), user.getEmail());
        UserInfo userInfoById = userService.getById(createdUser.getId());
        assertEquals(userInfoById.getUsername(), user.getUsername());
    }

    @Test
    void getByUsername_ShouldReturnUser() {
        User user = new User("test", Email.of("test@email.com"));
        userService.createNew(user.getUsername(), user.getEmail());
        UserInfo userInfo = userService.getByUsername(user.getUsername());
        assertEquals(userInfo.getUsername(), user.getUsername());
    }

    @Test
    void getByUsername_ShouldThrowExceptionWhenUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.getByUsername("test"));
    }

    @Test
    void getByEmail_ShouldReturnUser() {
        User user = new User("test", new Email("test@email.com"));
        userService.createNew(user.getUsername(), user.getEmail());
        UserInfo userInfo = userService.getByEmail(user.getEmail());
        assertEquals(userInfo.getUsername(), user.getUsername());
    }

    @Test
    void getByEmail_ShouldThrowExceptionWhenUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.getByEmail(new Email("test@email.com")));
    }

    @Test
    void verifyEmail_ShouldVerifyEmail() {
        User user = new User("test", Email.of("test@email.com"));
        User createdUser = (User) userService.createNew(user.getUsername(), user.getEmail());
        userService.verifyEmail(createdUser.getId());
        assertTrue(createdUser.isEmailVerified());
    }

    @Test
    void activate_ShouldActivateUser() {
        User user = new User("test", Email.of("test@email.com"));
        User createdUser = (User) userService.createNew(user.getUsername(), user.getEmail());
        userService.activate(createdUser.getId());
        assertTrue(createdUser.isActive());
    }
}