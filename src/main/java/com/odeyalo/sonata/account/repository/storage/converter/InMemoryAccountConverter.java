package com.odeyalo.sonata.account.repository.storage.converter;

import com.odeyalo.sonata.account.entity.Account;
import com.odeyalo.sonata.account.entity.InMemoryAccount;
import com.odeyalo.sonata.account.repository.RepositoryType;
import com.odeyalo.sonata.account.repository.storage.PersistableAccount;
import org.springframework.stereotype.Component;

@Component
public class InMemoryAccountConverter implements PersistableAccount2SpecificAccountConverter {

    @Override
    public Account convertTo(PersistableAccount account) {
        return InMemoryAccount.builder()
                .id(account.getId())
                .username(account.getUsername())
                .email(account.getEmail())
                .gender(account.getGender())
                .birthdate(account.getBirthdate())
                .creationTime(account.getCreationTime())
                .countryCode(account.getCountryCode())
                .build();
    }

    @Override
    public PersistableAccount convertFrom(Account account) {
        return PersistableAccount.builder()
                .id(account.getId())
                .username(account.getUsername())
                .email(account.getEmail())
                .gender(account.getGender())
                .birthdate(account.getBirthdate())
                .creationTime(account.getCreationTime())
                .countryCode(account.getCountryCode())
                .build();
    }

    @Override
    public RepositoryType supportedRepository() {
        return RepositoryType.IN_MEMORY;
    }
}
