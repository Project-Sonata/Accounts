package com.odeyalo.sonata.account.web.security.auth.support;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface AccessTokenExtractor {
    /**
     * Extract the access token from the exchange
     * @param exchange - current exchange
     * @return - extracted token or empty mono if not extracted
     */
    Mono<String> extractToken(ServerWebExchange exchange);
}