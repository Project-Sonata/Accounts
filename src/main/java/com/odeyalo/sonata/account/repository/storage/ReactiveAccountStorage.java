package com.odeyalo.sonata.account.repository.storage;

import com.odeyalo.sonata.account.repository.ReactiveAccountPersistentOperations;

/**
 * High level abstraction between repository and service.
 * The implementation can just delegate saving, do load balancing or anything else
 */
public interface ReactiveAccountStorage extends ReactiveAccountPersistentOperations<PersistableAccount> {
}
