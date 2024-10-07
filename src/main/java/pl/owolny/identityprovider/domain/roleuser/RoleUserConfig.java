package pl.owolny.identityprovider.domain.roleuser;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
class RoleUserConfig {

    private final RoleUserRepository roleUserRepository;

    public RoleUserConfig(RoleUserRepository roleUserRepository) {
        this.roleUserRepository = roleUserRepository;
    }

    @Bean
    RoleUserService roleUserService() {
        return new RoleUserServiceImpl(roleUserRepository);
    }
}
