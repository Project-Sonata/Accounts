package com.odeyalo.sonata.account.configuration;

import com.odeyalo.sonata.account.web.filter.UserAuthenticationWebFilter;
import com.odeyalo.sonata.account.web.security.auth.TokenServerAuthenticationConverter;
import com.odeyalo.sonata.account.web.security.auth.support.AccessTokenExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;

@Configuration
public class SecurityHelperBeansConfiguration {

    @Autowired
    ReactiveAuthenticationManager manager;



    @Bean
    public TokenServerAuthenticationConverter tokenServerAuthenticationConverter(AccessTokenExtractor accessTokenExtractor) {
        return new TokenServerAuthenticationConverter(accessTokenExtractor);
    }

    @Bean
    public AuthenticationWebFilter authenticationWebFilter(ServerAuthenticationConverter authenticationConverter) {
        return new UserAuthenticationWebFilter(authenticationConverter, manager);
    }
}
