package pl.owolny.identityprovider.domain.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
class UserConfiguration {

    private final UserRepository userRepository;

    UserConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    UserService userService() {
        return new UserServiceImpl(userRepository);
    }
}
