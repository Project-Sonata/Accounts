package com.odeyalo.sonata.account.repository;

import reactor.core.publisher.Mono;

/**
 * Basic persistent operations
 * @param <T> - entity
 * @param <ID> - id of the entity
 */
public interface ReactiveBasicPersistentOperations<T, ID>{
    Mono<T> save(T entity);

    Mono<T> findById(ID id);

    Mono<Void> deleteById(ID id);
}
