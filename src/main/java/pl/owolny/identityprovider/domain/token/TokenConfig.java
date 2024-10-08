package pl.owolny.identityprovider.domain.token;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration(proxyBeanMethods = false)
@EnableRedisRepositories(basePackages = "pl.owolny.identityprovider.domain.token")
class TokenConfig {

    private final OAuth2LinkingTokenRepository tokenRepository;

    public TokenConfig(OAuth2LinkingTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Bean
    OAuth2LinkingTokenService tokenService() {
        return new OAuth2LinkingTokenService(tokenRepository);
    }
}
