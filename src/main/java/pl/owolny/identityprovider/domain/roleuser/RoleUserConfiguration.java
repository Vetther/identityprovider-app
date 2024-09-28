package pl.owolny.identityprovider.domain.roleuser;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
class RoleUserConfiguration {

    private final RoleUserRepository roleUserRepository;

    public RoleUserConfiguration(RoleUserRepository roleUserRepository) {
        this.roleUserRepository = roleUserRepository;
    }

    @Bean
    RoleUserService roleUserService() {
        return new RoleUserServiceImpl(roleUserRepository);
    }
}
