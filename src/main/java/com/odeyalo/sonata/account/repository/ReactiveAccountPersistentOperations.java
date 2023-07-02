package com.odeyalo.sonata.account.repository;

import com.odeyalo.sonata.account.entity.Account;
import reactor.core.publisher.Mono;

/**
 * Operations for account entity
 * @param <T> - entity
 */
public interface ReactiveAccountPersistentOperations<T extends Account> extends ReactiveBasicPersistentOperations<T, String> {

    Mono<T> findAccountByEmail(String email);

    Mono<T> findAccountByUsername(String username);
}
