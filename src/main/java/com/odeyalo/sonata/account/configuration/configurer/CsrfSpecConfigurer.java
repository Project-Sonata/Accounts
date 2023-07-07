package com.odeyalo.sonata.account.configuration.configurer;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.stereotype.Component;

/**
 * Used to configure the {@link org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec}
 */
@Component
public class CsrfSpecConfigurer implements Customizer<ServerHttpSecurity.CsrfSpec> {

    @Override
    public void customize(ServerHttpSecurity.CsrfSpec csrfSpec) {
        csrfSpec.disable();
    }
}
