package pl.owolny.identityprovider.domain.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
class UserConfig {

    private final UserRepository userRepository;

    UserConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    UserService userService() {
        return new UserServiceImpl(userRepository);
    }
}
