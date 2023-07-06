package com.odeyalo.sonata.account.support.token;

import reactor.core.publisher.Mono;

/**
 * Base interface for access token validation.
 */
public interface ReactiveAccessTokenValidator {
    /**
     * Validate the access token and return the info about it
     * @param tokenValue - value of the token
     * @return - validated access token
     */
    Mono<ValidatedAccessToken> validateToken(String tokenValue);
}
