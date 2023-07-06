package com.odeyalo.sonata.account.configuration;

import com.odeyalo.sonata.account.web.filter.UserAuthenticationWebFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfiguration {

    @Autowired
    Customizer<ServerHttpSecurity.AuthorizeExchangeSpec> authorizeExchangeSpecCustomizer;

    @Autowired
    ReactiveAuthenticationManager manager;

    @Bean
    public AuthenticationWebFilter authenticationWebFilter() {
        return new UserAuthenticationWebFilter(manager);
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity httpSecurity) {
        return httpSecurity
                .cors((ServerHttpSecurity.CorsSpec::disable))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(authorizeExchangeSpecCustomizer)
                .authenticationManager(manager)
                .addFilterAt(authenticationWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
//                .securityContextRepository()
                .build();
    }


}
