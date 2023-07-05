package com.odeyalo.sonata.account.support.token;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * ReactiveAccessTokenValidator impl that validate the access token in remote service
 */
@Component
public class RemoteReactiveAccessTokenValidator implements ReactiveAccessTokenValidator {
    private final RemoteAccessTokenValidationStrategy validationStrategy;

    public RemoteReactiveAccessTokenValidator(RemoteAccessTokenValidationStrategy validationStrategy) {
        this.validationStrategy = validationStrategy;
    }

    @Override
    public Mono<ValidatedAccessToken> validateToken(String tokenValue) {
        return validationStrategy.validateAccessToken(tokenValue);
    }
}
