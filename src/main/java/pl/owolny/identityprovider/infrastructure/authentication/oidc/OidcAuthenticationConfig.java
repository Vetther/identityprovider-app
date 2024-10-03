package pl.owolny.identityprovider.infrastructure.authentication.oidc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;

@Configuration(proxyBeanMethods = false)
class OidcAuthenticationConfig {

    @Bean
    AuthenticationProvider oidcAuthenticationProvider() {
        return new OidcAuthenticationProvider();
    }
}
