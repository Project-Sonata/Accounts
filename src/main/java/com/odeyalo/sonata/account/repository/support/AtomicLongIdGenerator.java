package com.odeyalo.sonata.account.repository.support;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class AtomicLongIdGenerator implements IdGenerator {
    private final AtomicLong idHolder = new AtomicLong();

    @Override
    public Mono<String> nextId() {
        return Mono.just(String.valueOf(idHolder.incrementAndGet()));
    }
}
