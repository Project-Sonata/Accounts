package com.odeyalo.sonata.account.repository;

import com.odeyalo.sonata.account.entity.Account;

/**
 * Repository to save specific impl of account entity
 * @param <T> - entity
 */
public interface ReactiveAccountRepository<T extends Account> extends ReactiveAccountPersistentOperations<T> {

    RepositoryType getRepositoryType();

}
