package com.odeyalo.sonata.account.support.token;

import reactor.core.publisher.Mono;

/**
 * Strategy to validate the token in remote service
 */
public interface RemoteAccessTokenValidationStrategy {

    Mono<ValidatedAccessToken> validateAccessToken(String tokenValue);

}
