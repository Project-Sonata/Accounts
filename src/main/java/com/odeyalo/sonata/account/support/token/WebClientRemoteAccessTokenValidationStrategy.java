package com.odeyalo.sonata.account.support.token;

import com.odeyalo.sonata.common.authorization.TokenIntrospectionRequest;
import com.odeyalo.sonata.common.authorization.TokenIntrospectionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * {@link RemoteAccessTokenValidationStrategy} implementation that uses {@link WebClient} to send HTTP request to validate the token.
 */
public class WebClientRemoteAccessTokenValidationStrategy implements RemoteAccessTokenValidationStrategy {
    private final Logger logger = LoggerFactory.getLogger(WebClientRemoteAccessTokenValidationStrategy.class);
    private final WebClient tokenValidationWebClient;

    public WebClientRemoteAccessTokenValidationStrategy(WebClient tokenValidationWebClient) {
        this.tokenValidationWebClient = tokenValidationWebClient;
    }

    @Override
    public Mono<ValidatedAccessToken> validateAccessToken(String tokenValue) {
        return tokenValidationWebClient.post()
                .uri("/token/info")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(TokenIntrospectionRequest.of(tokenValue)), TokenIntrospectionRequest.class)
                .exchangeToMono(response -> response.bodyToMono(TokenIntrospectionResponse.class))
                .map(this::convertTo);
    }

    private ValidatedAccessToken convertTo(TokenIntrospectionResponse body) {
        if (!body.isValid()) {
            return ValidatedAccessToken.invalid();
        }
        AccessTokenMetadata metadata = AccessTokenMetadata.of(body.getUserId(), body.getScope().split(" "),
                body.getIssuedAt(), body.getExpiresIn());

        return ValidatedAccessToken.valid(metadata);
    }
}