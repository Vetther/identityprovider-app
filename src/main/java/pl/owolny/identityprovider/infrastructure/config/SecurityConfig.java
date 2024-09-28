package pl.owolny.identityprovider.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;

    SecurityConfig(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/css/**", "/images/**").permitAll()
                                .requestMatchers("/webjars/**").permitAll()
                                .requestMatchers("/error").permitAll()
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/logout").permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .formLogin(login -> login
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                )
                .authenticationProvider(authenticationProvider)
                .build();
    }
}
