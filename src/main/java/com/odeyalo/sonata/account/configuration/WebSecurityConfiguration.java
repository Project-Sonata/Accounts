package com.odeyalo.sonata.account.configuration;

import com.odeyalo.suite.security.annotation.EnableSuiteSecurity;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

@Configuration
@EnableWebFluxSecurity
@EnableSuiteSecurity
@Builder
public class WebSecurityConfiguration {
    @Autowired
    Customizer<ServerHttpSecurity.AuthorizeExchangeSpec> authorizeExchangeSpecCustomizer;
    @Autowired
    Customizer<ServerHttpSecurity.ExceptionHandlingSpec> exceptionHandlingSpecCustomizer;
    @Autowired
    Customizer<ServerHttpSecurity.CorsSpec> corsSpecCustomizer;
    @Autowired
    Customizer<ServerHttpSecurity.CsrfSpec> csrfSpecCustomizer;
    @Autowired
    AuthenticationWebFilter authenticationWebFilter;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity httpSecurity) {
        return httpSecurity
                .cors(corsSpecCustomizer)
                .csrf(csrfSpecCustomizer)
                .authorizeExchange(authorizeExchangeSpecCustomizer)
                .exceptionHandling(exceptionHandlingSpecCustomizer)
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}