package com.odeyalo.sonata.account.web.security.auth;

import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;

/**
 * Represent always authenticated subject
 */
public interface AuthenticatedSubject extends AuthenticatedPrincipal, Authentication, CredentialsContainer {
    @Override
    AuthenticatedUserDetails getDetails();

    @Override
    default boolean isAuthenticated() {
        return true;
    }
}
