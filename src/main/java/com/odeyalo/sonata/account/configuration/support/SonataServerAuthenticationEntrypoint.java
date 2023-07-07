package com.odeyalo.sonata.account.configuration.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odeyalo.sonata.account.dto.ExceptionMessage;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class SonataServerAuthenticationEntrypoint implements ServerAuthenticationEntryPoint {
    public static final String EXCEPTION_DESCRIPTION = "Missing access token or token has been expired";
    private final ObjectMapper objectMapper;

    @Autowired
    public SonataServerAuthenticationEntrypoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        prepareUnauthorizedRequest(exchange);
        DataBuffer buffer = getBody(exchange);
        return exchange.getResponse().writeWith(Flux.just(buffer));
    }

    @NotNull
    @SneakyThrows
    private DataBuffer getBody(ServerWebExchange exchange) {
        ExceptionMessage message = ExceptionMessage.of(EXCEPTION_DESCRIPTION);
        String body = objectMapper.writeValueAsString(message);
        return exchange.getResponse().bufferFactory().wrap(body.getBytes());
    }

    private void prepareUnauthorizedRequest(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(401));
        exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }
}
