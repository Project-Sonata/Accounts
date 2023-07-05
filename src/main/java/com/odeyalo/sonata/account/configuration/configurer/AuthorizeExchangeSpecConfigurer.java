package com.odeyalo.sonata.account.configuration.configurer;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.stereotype.Component;

/**
 * Helper class to configure the {@link ServerHttpSecurity.AuthorizeExchangeSpec}
 */
@Component
public class AuthorizeExchangeSpecConfigurer implements Customizer<ServerHttpSecurity.AuthorizeExchangeSpec> {

    @Override
    public void customize(ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchangeSpec) {
        authorizeExchangeSpec
                .pathMatchers(HttpMethod.GET, "/account/me")
                .hasAuthority("SCOPE_ACCOUNT_READ")
                .anyExchange()
                .permitAll();
    }
}
