package com.taskengine.taskengine_api_gateway.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.security.PublicKey;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain FilterChain(ServerHttpSecurity http){
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange->exchange
                        .pathMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .anyExchange().authenticated())
                .oauth2ResourceServer(oauth->oauth.jwt(Customizer.withDefaults()))
                .build();
    }

    private static final String[] PUBLIC_ENDPOINTS={
            "/user/api/v1/user/register",
            "/auth/api/v1/login/token",
            "/auth/api/v1/oauth/token",
            "/auth/api/v1/client/register",
            "/auth/api/v1/.well-known/jwks.json"
    };
}


