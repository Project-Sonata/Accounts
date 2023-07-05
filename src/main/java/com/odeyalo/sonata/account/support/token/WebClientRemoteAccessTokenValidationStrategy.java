package com.odeyalo.sonata.account.support.token;

import com.odeyalo.sonata.account.support.token.converter.ValidatedAccessTokenConverter;
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
    private final ValidatedAccessTokenConverter<TokenIntrospectionResponse> accessTokenConverter;

    public WebClientRemoteAccessTokenValidationStrategy(WebClient tokenValidationWebClient, ValidatedAccessTokenConverter<TokenIntrospectionResponse> accessTokenConverter) {
        this.tokenValidationWebClient = tokenValidationWebClient;
        this.accessTokenConverter = accessTokenConverter;
    }

    @Override
    public Mono<ValidatedAccessToken> validateAccessToken(String tokenValue) {
        return tokenValidationWebClient.post()
                .uri("/token/info")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(TokenIntrospectionRequest.of(tokenValue)), TokenIntrospectionRequest.class)
                .exchangeToMono(response -> response.bodyToMono(TokenIntrospectionResponse.class))
                .map(accessTokenConverter::convertTo);
    }
}