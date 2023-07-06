package com.odeyalo.sonata.account.web.security.auth;

import lombok.Value;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Represent the authentication with Token
 */
@Value
public class TokenAuthentication extends AbstractAuthenticationToken {
    Object principal;
    String token;

    public TokenAuthentication(Object principal, String token) {
        super(null);
        this.principal = principal;
        this.token = token;
    }

    public TokenAuthentication(Object principal, String token, Collection<GrantedAuthority> grantedAuthorities) {
        super(grantedAuthorities);
        this.principal = principal;
        this.token = token;
    }

    public static TokenAuthentication unauthenticated(String token) {
        return new TokenAuthentication(token, token);
    }

    public static TokenAuthentication authenticated(TokenAuthentication tokenAuthentication, Collection<GrantedAuthority> grantedAuthorities) {
        return TokenAuthentication.authenticated(tokenAuthentication.getPrincipal(), tokenAuthentication.getToken(), grantedAuthorities);
    }
    public static TokenAuthentication authenticated(String token, Collection<GrantedAuthority> grantedAuthorities) {
        return authenticated(token, token, grantedAuthorities);
    }

    public static TokenAuthentication authenticated(Object principal, String token, Collection<GrantedAuthority> grantedAuthorities) {
        TokenAuthentication authentication = new TokenAuthentication(principal, token, grantedAuthorities);
        authentication.setAuthenticated(true);
        return authentication;
    }

    public String getToken() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}