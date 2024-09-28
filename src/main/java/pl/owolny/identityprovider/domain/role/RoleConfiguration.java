package pl.owolny.identityprovider.domain.role;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
class RoleConfiguration {

    private final RoleRepository roleRepository;

    RoleConfiguration(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Bean
    RoleService roleService() {
        return new RoleServiceImpl(roleRepository);
    }
}
