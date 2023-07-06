package com.odeyalo.sonata.account.web.filter;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;

/**
 * Filter to authenticate the user. Delegates all the job to the {@link AuthenticationWebFilter}
 */
public class UserAuthenticationWebFilter extends AuthenticationWebFilter {

    public UserAuthenticationWebFilter(ServerAuthenticationConverter converter, ReactiveAuthenticationManager authenticationManager) {
        super(authenticationManager);
        setServerAuthenticationConverter(converter);
    }
}
