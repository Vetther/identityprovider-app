package pl.owolny.identityprovider.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.util.List;

@EnableWebSecurity
@Configuration
class SecurityConfig {

    private final List<AuthenticationProvider> authenticationProviders;
    private final AuthenticationFailureHandler authenticationFailureHandler;

    SecurityConfig(List<AuthenticationProvider> authenticationProviders, AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationProviders = authenticationProviders;
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    @Bean
    @Order(2)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/css/**", "/images/**").permitAll()
                        .requestMatchers("/webjars/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/verify").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .failureHandler(this.authenticationFailureHandler)
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/login")
                        .failureHandler(this.authenticationFailureHandler)
                )
                .authenticationManager(new ProviderManager(this.authenticationProviders))
                .build();
    }
}
