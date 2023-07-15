package com.odeyalo.sonata.account.service.password;

import com.odeyalo.sonata.account.web.security.auth.AuthenticatedUser;
import reactor.core.publisher.Mono;

/**
 * Strategy to update password in remote service
 */
public interface RemotePasswordUpdaterStrategy {
    /**
     * Update password remotely
     * @param user - user to update password to
     * @param container - container with password
     * @return result of password updating
     */
    Mono<PasswordUpdatingResult> updatePassword(AuthenticatedUser user, PasswordContainer container);
}