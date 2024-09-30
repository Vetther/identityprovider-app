package pl.owolny.identityprovider.infrastructure.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration(proxyBeanMethods = false)
public class JwkConfig {

    private final RSAPrivateKey privateKeyResource;
    private final RSAPublicKey publicKeyResource;

    public JwkConfig(@Value("classpath:keys/private.key") RSAPrivateKey privateKeyResource,
                     @Value("classpath:keys/public.pub") RSAPublicKey publicKeyResource) {
        this.privateKeyResource = privateKeyResource;
        this.publicKeyResource = publicKeyResource;
    }

    public JWKSet getJwkSet() {
        return new JWKSet(new RSAKey.Builder(this.publicKeyResource)
                .privateKey(this.privateKeyResource)
                .build());
    }
}

