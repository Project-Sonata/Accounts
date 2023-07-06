package com.odeyalo.sonata.account.web.security.auth.support;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Extract the token from the authorization header. By default, Bearer prefix is used, but it can be overloaded using {@link #setPrefix(String)}
 * Note, that if there is no Prefix presented in Authorization header - {@link #extractToken(ServerWebExchange)} will return empty Mono
 */
@Component
public class PrefixedAuthorizationHeaderAccessTokenExtractor implements AccessTokenExtractor {
    public final static String BEARER = "Bearer";
    private String prefix = BEARER;

    @Override
    public Mono<String> extractToken(ServerWebExchange exchange) {
        return Mono.fromCallable(() -> {
                    String authorizationHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                    if (authorizationHeader == null || !authorizationHeader.startsWith(prefix)) {
                        return null;
                    }
                    return authorizationHeader.substring(prefix.length()).strip();
                }
        );
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("The prefix cannot be null!");
        }
        this.prefix = prefix.strip();
    }
}
