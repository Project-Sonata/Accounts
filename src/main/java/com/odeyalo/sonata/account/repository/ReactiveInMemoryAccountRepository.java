package com.odeyalo.sonata.account.repository;

import com.odeyalo.sonata.account.entity.InMemoryAccount;
import com.odeyalo.sonata.account.repository.support.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.stream.Collectors.toMap;

@Component
public class ReactiveInMemoryAccountRepository implements ReactiveAccountRepository<InMemoryAccount>, Clearable {
    private final ConcurrentMap<String, InMemoryAccount> accounts;
    private final ConcurrentMap<String, String> accountsByUsername;
    private final ConcurrentMap<String, String> accountsByEmail;
    private final IdGenerator idGenerator;

    @Autowired
    public ReactiveInMemoryAccountRepository(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
        this.accounts = new ConcurrentHashMap<>();
        this.accountsByEmail = new ConcurrentHashMap<>();
        this.accountsByUsername = new ConcurrentHashMap<>();
    }

    public ReactiveInMemoryAccountRepository(List<InMemoryAccount> accounts, IdGenerator idGenerator) {
        this.accounts = new ConcurrentHashMap<>(accounts.stream().collect(toMap(InMemoryAccount::getId, account -> account)));
        this.idGenerator = idGenerator;
        this.accountsByEmail = new ConcurrentHashMap<>();
        this.accountsByUsername = new ConcurrentHashMap<>();

        accounts.forEach(this::saveAccountIndexes);
    }

    public ReactiveInMemoryAccountRepository(ConcurrentMap<String, InMemoryAccount> accounts, IdGenerator idGenerator) {
        this.accounts = accounts;
        this.idGenerator = idGenerator;
        this.accountsByEmail = new ConcurrentHashMap<>();
        this.accountsByUsername = new ConcurrentHashMap<>();

        accounts.forEach((key, account) -> {
            saveAccountIndexes(account);
        });
    }

    @Override
    public Mono<InMemoryAccount> findAccountByEmail(String email) {
        return Mono.fromCallable(() -> accountsByEmail.get(email))
                .map(accounts::get);
    }

    @Override
    public Mono<InMemoryAccount> findAccountByUsername(String username) {
        return Mono.fromCallable(() -> accountsByUsername.get(username))
                .map(accounts::get);
    }

    @Override
    public Mono<InMemoryAccount> save(InMemoryAccount entity) {
        return Mono.from(doSaveAccount(entity))
                .log()
                .checkpoint("Saved the account to database!");
    }

    @Override
    public Mono<InMemoryAccount> findById(String id) {
        return Mono.fromCallable(() -> accounts.get(id));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return Mono.fromCallable(() -> accounts.remove(id))
                .then();
    }

    @Override
    public RepositoryType getRepositoryType() {
        return RepositoryType.IN_MEMORY;
    }

    protected Mono<InMemoryAccount> doSaveAccount(InMemoryAccount account) {
        return Mono.just(account)
                .flatMap(acc -> Mono.justOrEmpty(acc.getId()))
                .switchIfEmpty(idGenerator.nextId())
                .map(id -> {
                    account.setId(id);
                    return account;
                })
                .map(this::save0);
    }

    protected InMemoryAccount save0(InMemoryAccount account) {
        this.accounts.put(account.getId(), account);
        saveAccountIndexes(account);
        return account;
    }

    protected void saveAccountIndexes(InMemoryAccount account) {
        accountsByEmail.put(account.getEmail(), account.getId());
        accountsByUsername.put(account.getUsername(), account.getId());
    }

    @Override
    public Mono<Void> clear() {
        return Mono.fromRunnable(() -> {
                    accounts.clear();
                    accountsByUsername.clear();
                    accountsByEmail.clear();
                }
        ).then();
    }
}
