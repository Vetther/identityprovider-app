package pl.owolny.identityprovider.domain.token;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration(proxyBeanMethods = false)
@EnableRedisRepositories(basePackages = "pl.owolny.identityprovider.domain.token")
class TokenConfig {

    private final TokenRepository<OAuth2LinkingToken> tokenRepository;

    public TokenConfig(TokenRepository<OAuth2LinkingToken> tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Bean
    OAuth2LinkingTokenService tokenService() {
        return new OAuth2LinkingTokenService(tokenRepository);
    }
}
