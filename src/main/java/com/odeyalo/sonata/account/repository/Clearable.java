package com.odeyalo.sonata.account.repository;

import reactor.core.publisher.Mono;

/**
 * Indicates that some data can be cleared
 */
public interface Clearable {

    Mono<Void> clear();
}
