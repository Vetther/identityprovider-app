package pl.owolny.identityprovider.domain.credentials;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration(proxyBeanMethods = false)
class CredentialsConfig {

    private final CredentialsRepository credentialsRepository;

    CredentialsConfig(CredentialsRepository credentialsRepository) {
        this.credentialsRepository = credentialsRepository;
    }

    @Bean
    CredentialsService credentialsService() {
        return new CredentialsServiceImpl(encoder(), credentialsRepository);
    }
    
    PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
