package pl.owolny.identityprovider.domain.userprofile;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
class UserProfileConfig {

    private final UserProfileRepository userProfileRepository;

    public UserProfileConfig(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Bean
    UserProfileService userProfileService() {
        return new UserProfileServiceImpl(userProfileRepository);
    }
}
