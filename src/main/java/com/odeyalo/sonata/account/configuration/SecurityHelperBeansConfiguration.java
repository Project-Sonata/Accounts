package com.odeyalo.sonata.account.configuration;

import com.odeyalo.sonata.account.web.filter.UserAuthenticationWebFilter;
import com.odeyalo.sonata.account.web.security.auth.TokenServerAuthenticationConverter;
import com.odeyalo.sonata.account.web.security.auth.support.AccessTokenExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;

@Configuration
public class SecurityHelperBeansConfiguration {

    @Autowired
    ReactiveAuthenticationManager manager;

    @Bean
    public ServerAuthenticationFailureHandler serverAuthenticationFailureHandler(ServerAuthenticationEntryPoint entryPoint) {
        return new ServerAuthenticationEntryPointFailureHandler(entryPoint);
    }

    @Bean
    public TokenServerAuthenticationConverter tokenServerAuthenticationConverter(AccessTokenExtractor accessTokenExtractor) {
        return new TokenServerAuthenticationConverter(accessTokenExtractor);
    }

    @Bean
    public AuthenticationWebFilter authenticationWebFilter(ServerAuthenticationConverter authenticationConverter,
                                                           ServerAuthenticationFailureHandler failureHandler) {
        return new UserAuthenticationWebFilter(authenticationConverter, manager, failureHandler);
    }
}
