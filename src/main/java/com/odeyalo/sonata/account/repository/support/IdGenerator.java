package com.odeyalo.sonata.account.repository.support;

import reactor.core.publisher.Mono;

public interface IdGenerator {

    Mono<String> nextId();

}
