package com.odeyalo.sonata.account.web.security.auth;

import com.odeyalo.sonata.account.web.security.auth.support.AccessTokenExtractor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Convert token from the request to authentication
 */
public class TokenServerAuthenticationConverter implements ServerAuthenticationConverter {
    private final AccessTokenExtractor tokenExtractor;

    public TokenServerAuthenticationConverter(AccessTokenExtractor tokenExtractor) {
        this.tokenExtractor = tokenExtractor;
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return tokenExtractor.extractToken(exchange)
                .map(TokenAuthentication::unauthenticated);
    }
}
