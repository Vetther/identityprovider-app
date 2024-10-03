package pl.owolny.identityprovider.infrastructure.authentication.oauth2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import pl.owolny.identityprovider.domain.federatedidentity.FederatedIdentityService;
import pl.owolny.identityprovider.domain.role.RoleService;
import pl.owolny.identityprovider.domain.roleuser.RoleUserService;
import pl.owolny.identityprovider.domain.user.UserService;
import pl.owolny.identityprovider.infrastructure.authentication.oauth2.map.OAuth2UserMapper;

import java.util.Map;

@Configuration(proxyBeanMethods = false)
class OAuth2AuthenticationConfig {

    private final UserService userService;
    private final FederatedIdentityService federatedIdentityService;
    private final RoleUserService roleUserService;
    private final RoleService roleService;
    private final Map<String, OAuth2UserMapper> mappers;

    OAuth2AuthenticationConfig(UserService userService, FederatedIdentityService federatedIdentityService, RoleUserService roleUserService, RoleService roleService, Map<String, OAuth2UserMapper> mappers) {
        this.userService = userService;
        this.federatedIdentityService = federatedIdentityService;
        this.roleUserService = roleUserService;
        this.roleService = roleService;
        this.mappers = mappers;
    }

    @Bean
    AuthenticationProvider oAuth2AuthenticationProvider() {
        return new OAuth2AuthenticationProvider(this.userService, this.federatedIdentityService, this.roleUserService, this.roleService, mappers);
    }
}
