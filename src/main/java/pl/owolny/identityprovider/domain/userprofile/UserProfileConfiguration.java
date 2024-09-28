package pl.owolny.identityprovider.domain.userprofile;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
class UserProfileConfiguration {

    private final UserProfileRepository userProfileRepository;

    public UserProfileConfiguration(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Bean
    UserProfileService userProfileService() {
        return new UserProfileServiceImpl(userProfileRepository);
    }
}
