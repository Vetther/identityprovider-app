package pl.owolny.identityprovider.infrastructure.authentication.oauth2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;

@Configuration(proxyBeanMethods = false)
class OAuth2AuthenticationConfig {

    @Bean
    AuthenticationProvider oAuth2AuthenticationProvider() {
        return new OAuth2AuthenticationProvider();
    }
}
