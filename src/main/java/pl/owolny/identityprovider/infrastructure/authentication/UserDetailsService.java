package pl.owolny.identityprovider.infrastructure.authentication;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.owolny.identityprovider.domain.role.RoleId;
import pl.owolny.identityprovider.domain.role.RoleInfo;
import pl.owolny.identityprovider.domain.role.RoleService;
import pl.owolny.identityprovider.domain.roleuser.RoleUserService;
import pl.owolny.identityprovider.domain.user.UserInfo;
import pl.owolny.identityprovider.domain.user.UserService;
import pl.owolny.identityprovider.domain.user.exception.UserNotFoundException;
import pl.owolny.identityprovider.vo.Email;

import java.util.Set;
import java.util.stream.Collectors;

class UserDetailsService {

    private final UserService userService;
    private final RoleUserService roleUserService;
    private final RoleService roleService;

    public UserDetailsService(UserService userService, RoleUserService roleUserService, RoleService roleService) {
        this.userService = userService;
        this.roleUserService = roleUserService;
        this.roleService = roleService;
    }

    public AuthenticatedUser loadUser(String usernameOrEmail) throws UsernameNotFoundException {

        UserInfo userInfo = this.getUserInfo(usernameOrEmail);
        Set<RoleInfo> userRoles = this.getUserRoles(userInfo);

        return new AuthenticatedUser(
                userInfo.getId(),
                userRoles.stream()
                        .flatMap(role -> role.getAuthorities().stream())
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet()));
    }

    private UserInfo getUserInfo(String usernameOrEmail) {
        try {
            if (Email.isValid(usernameOrEmail)) {
                return this.userService.getByEmail(new Email(usernameOrEmail));
            } else {
                return this.userService.getByUsername(usernameOrEmail);
            }
        }
        catch (UserNotFoundException e) {
            throw new UsernameNotFoundException("User not found: " + usernameOrEmail, e);
        }
    }

    private Set<RoleInfo> getUserRoles(UserInfo userInfo) {
        Set<RoleId> userRoles = this.roleUserService.getUserRoles(userInfo.getId());
        return userRoles.stream()
                .map(this.roleService::getById)
                .collect(Collectors.toSet());
    }

}
