package com.odeyalo.sonata.account.repository.storage;

import com.odeyalo.sonata.account.entity.Account;
import com.odeyalo.sonata.account.repository.ReactiveAccountRepository;
import com.odeyalo.sonata.account.repository.storage.converter.AccountConvertersRegistry;
import com.odeyalo.sonata.account.repository.storage.converter.PersistableAccount2SpecificAccountConverter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * {@link ReactiveAccountStorage} that delegates the job to the first impl
 */
@Component
public class DeleagatingReactiveAccountStorage implements ReactiveAccountStorage {
    private final ReactiveAccountRepository<Account> repository;
    private final AccountConvertersRegistry container;

    public DeleagatingReactiveAccountStorage(List<ReactiveAccountRepository<? extends Account>> repositories, AccountConvertersRegistry container) {
        this.repository = (ReactiveAccountRepository<Account>) repositories.get(0);
        this.container = container;
    }

    @Override
    public Mono<PersistableAccount> findAccountByEmail(String email) {
        return repository.findById(email)
                .map(account -> getConverter().convertFrom(account));
    }


    @Override
    public Mono<PersistableAccount> findAccountByUsername(String username) {
        return repository.findAccountByUsername(username)
                .map(account -> getConverter().convertFrom(account));
    }

    @Override
    public Mono<PersistableAccount> save(PersistableAccount entity) {
        return Mono.fromCallable(() -> getConverter().convertTo(entity))
                .flatMap(repository::save)
                .map(account -> getConverter().convertFrom(account));
    }

    @Override
    public Mono<PersistableAccount> findById(String id) {
        return repository.findById(id)
                .map(account -> getConverter().convertFrom(account));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }

    private PersistableAccount2SpecificAccountConverter getConverter() {
        return container.getConverter(repository.getRepositoryType());
    }
}
