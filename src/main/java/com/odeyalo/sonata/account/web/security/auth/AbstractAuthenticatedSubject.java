package com.odeyalo.sonata.account.web.security.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Base class for authenticated subjects objects.
 * Implementations which use this class should be immutable.
 */
public class AbstractAuthenticatedSubject extends AbstractAuthenticationToken implements AuthenticatedSubject {
    protected Object principal;
    protected AuthenticatedUserDetails details;
    protected Object credentials;

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     * @param credentials - credentials for user
     * @param details   -  details about subject
     */
    public AbstractAuthenticatedSubject(AuthenticatedUserDetails details, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.credentials = credentials;
        this.details = details;
    }

    public AbstractAuthenticatedSubject(Object principal, AuthenticatedUserDetails details, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.details = details;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public AuthenticatedUserDetails getDetails() {
        return details;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}