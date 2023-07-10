package com.odeyalo.sonata.account.web.security.auth;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

/**
 * Represent authenticated user
 */
@EqualsAndHashCode(callSuper = true)
@Value
public class AuthenticatedUser extends AbstractAuthenticatedSubject {

    public AuthenticatedUser(AuthenticatedUserDetails details, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(details, credentials, authorities);
    }

    public AuthenticatedUser(Object principal, AuthenticatedUserDetails details, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, details, credentials, authorities);
    }

    public static AuthenticatedUser of(AuthenticatedUserDetails details, Object credentials, Set<GrantedAuthority> authorities) {
        return new AuthenticatedUser(details, credentials, authorities);
    }

    public static AuthenticatedUser of(Object principal, AuthenticatedUserDetails details, Object credentials, Set<GrantedAuthority> authorities) {
        return new AuthenticatedUser(principal, details, credentials, authorities);
    }
}
