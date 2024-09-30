package pl.owolny.identityprovider.infrastructure.authentication.credentials;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import pl.owolny.identityprovider.domain.credentials.CredentialsService;
import pl.owolny.identityprovider.domain.role.RoleService;
import pl.owolny.identityprovider.domain.roleuser.RoleUserService;
import pl.owolny.identityprovider.domain.user.UserService;

@Configuration(proxyBeanMethods = false)
class CredentialsAuthenticationConfig {

    private final CredentialsService credentialsService;
    private final UserService userService;
    private final RoleUserService roleUserService;
    private final RoleService roleService;

    CredentialsAuthenticationConfig(CredentialsService credentialsService, UserService userService, RoleUserService roleUserService, RoleService roleService) {
        this.credentialsService = credentialsService;
        this.userService = userService;
        this.roleUserService = roleUserService;
        this.roleService = roleService;
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        return new CredentialsAuthenticationProvider(
                this.credentialsService,
                new CredentialsUserService(this.userService, this.roleUserService, this.roleService)
        );
    }
}
