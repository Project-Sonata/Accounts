package com.odeyalo.sonata.account.support.token;

import com.odeyalo.sonata.account.support.token.converter.ValidatedAccessTokenConverter;
import com.odeyalo.sonata.common.authorization.TokenIntrospectionRequest;
import com.odeyalo.sonata.common.authorization.TokenIntrospectionResponse;
import com.odeyalo.sonata.suite.reactive.client.ReactiveTokenIntrospectionClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Uses the {@link ReactiveTokenIntrospectionClient} to introspect the token
 */
@Component
public class SuiteClientRemoteAccessTokenValidationStrategy implements RemoteAccessTokenValidationStrategy {
    private final ReactiveTokenIntrospectionClient tokenIntrospectionClient;
    private final ValidatedAccessTokenConverter<TokenIntrospectionResponse> accessTokenConverter;

    @Autowired
    public SuiteClientRemoteAccessTokenValidationStrategy(ReactiveTokenIntrospectionClient tokenIntrospectionClient, ValidatedAccessTokenConverter<TokenIntrospectionResponse> accessTokenConverter) {
        this.tokenIntrospectionClient = tokenIntrospectionClient;
        this.accessTokenConverter = accessTokenConverter;
    }

    @Override
    public Mono<ValidatedAccessToken> validateAccessToken(String tokenValue) {
        return tokenIntrospectionClient.introspectToken(TokenIntrospectionRequest.of(tokenValue))
                .flatMap(r -> r.getBody() != null ? r.getBody() : Mono.empty())
                .map(accessTokenConverter::convertTo);
    }
}
