package com.odeyalo.sonata.account.repository.storage.converter;

import com.odeyalo.sonata.account.repository.RepositoryType;

public interface AccountConvertersRegistry {

    void addConverter(PersistableAccount2SpecificAccountConverter converter);

    PersistableAccount2SpecificAccountConverter getConverter(RepositoryType type);

    boolean contains(RepositoryType type);
}
