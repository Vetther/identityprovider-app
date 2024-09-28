package pl.owolny.identityprovider.infrastructure.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import pl.owolny.identityprovider.domain.credentials.CredentialsService;
import pl.owolny.identityprovider.domain.role.RoleService;
import pl.owolny.identityprovider.domain.roleuser.RoleUserService;
import pl.owolny.identityprovider.domain.user.UserService;

@Configuration(proxyBeanMethods = false)
class AuthenticationConfiguration {

    private final CredentialsService credentialsService;
    private final UserService userService;
    private final RoleUserService roleUserService;
    private final RoleService roleService;

    AuthenticationConfiguration(CredentialsService credentialsService, UserService userService, RoleUserService roleUserService, RoleService roleService) {
        this.credentialsService = credentialsService;
        this.userService = userService;
        this.roleUserService = roleUserService;
        this.roleService = roleService;
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        return new AuthenticationProviderImpl(
                this.credentialsService,
                new UserDetailsService(this.userService, this.roleUserService, this.roleService)
        );
    }
}
