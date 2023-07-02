package com.odeyalo.sonata.account.repository.storage.converter;

import com.odeyalo.sonata.account.entity.Account;
import com.odeyalo.sonata.account.repository.RepositoryType;
import com.odeyalo.sonata.account.repository.storage.PersistableAccount;

/**
 * Convert the {@link PersistableAccount} to {@link Account} and vice-versa
 */
public interface PersistableAccount2SpecificAccountConverter {

    Account convertTo(PersistableAccount account);

    PersistableAccount convertFrom(Account account);

    RepositoryType supportedRepository();
}
