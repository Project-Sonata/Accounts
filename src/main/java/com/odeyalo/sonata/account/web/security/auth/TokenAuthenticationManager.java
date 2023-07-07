package com.odeyalo.sonata.account.web.security.auth;

import com.odeyalo.sonata.account.exception.InvalidAccessTokenException;
import com.odeyalo.sonata.account.support.token.ReactiveAccessTokenValidator;
import com.odeyalo.sonata.account.support.token.ValidatedAccessToken;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Used to validate the access token
 */
@Component
public class TokenAuthenticationManager implements ReactiveAuthenticationManager {
    private final ReactiveAccessTokenValidator accessTokenValidator;
    private final Logger logger = LoggerFactory.getLogger(TokenAuthenticationManager.class);

    public TokenAuthenticationManager(ReactiveAccessTokenValidator accessTokenValidator) {
        this.accessTokenValidator = accessTokenValidator;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        if (!(authentication instanceof TokenAuthentication tokenAuthentication)) {
            return Mono.empty();
        }
        String accessToken = tokenAuthentication.getToken();
        if (accessToken == null) {
            return Mono.empty();
        }
        return accessTokenValidator.validateToken(accessToken)
                .doOnError(err -> logger.error("Failed to validate the token: \n", err))
                .flatMap(validatedAccessToken -> {
                    if (validatedAccessToken.isValid()) {
                        Set<GrantedAuthority> authorities = convertScopesToAuthorities(validatedAccessToken);
                        return Mono.just(TokenAuthentication.authenticated(tokenAuthentication, authorities));
                    }
                    return Mono.error(new InvalidAccessTokenException("Invalid access token!"));
                });
    }

    @NotNull
    private static Set<GrantedAuthority> convertScopesToAuthorities(ValidatedAccessToken validatedAccessToken) {
        return Arrays.stream(validatedAccessToken.getToken().getScopes()).map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
