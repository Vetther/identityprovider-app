package pl.owolny.identityprovider.domain.federatedidentity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
class FederatedIdentityConfig {

    private final FederatedIdentityRepository federatedIdentityRepository;

    public FederatedIdentityConfig(FederatedIdentityRepository federatedIdentityRepository) {
        this.federatedIdentityRepository = federatedIdentityRepository;
    }

    @Bean
    FederatedIdentityService federatedIdentityService() {
        return new FederatedIdentityServiceImpl(federatedIdentityRepository);
    }
}
