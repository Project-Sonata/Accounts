package com.odeyalo.sonata.account.service.password;

import com.odeyalo.suite.security.auth.AuthenticatedUser;
import reactor.core.publisher.Mono;

/**
 * Interface to update the user's password if and only if provided old password is valid
 */
public interface SecurePasswordUpdater {
    /**
     * Update the user's password if and only if {@link PasswordContainer#getOldPassword} matches old user's password
     * @param user - user to update password to
     * @param container - container with passwords
     * @return - result of the password updating
     */
    Mono<PasswordUpdatingResult> updatePassword(AuthenticatedUser user, PasswordContainer container);
}