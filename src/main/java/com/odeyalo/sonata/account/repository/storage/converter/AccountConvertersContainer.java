package com.odeyalo.sonata.account.repository.storage.converter;

import com.odeyalo.sonata.account.repository.RepositoryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Component
public class AccountConvertersContainer implements AccountConvertersRegistry {
    private final Map<RepositoryType, PersistableAccount2SpecificAccountConverter> converters;

    public  AccountConvertersContainer() {
        this.converters = new HashMap<>();
    }
    @Autowired
    public AccountConvertersContainer(List<PersistableAccount2SpecificAccountConverter> converters) {
        this.converters = converters.stream()
                .collect(toMap(
                        PersistableAccount2SpecificAccountConverter::supportedRepository,
                        acc -> acc)
                );
    }

    public AccountConvertersContainer(Map<RepositoryType, PersistableAccount2SpecificAccountConverter> converters) {
        this.converters = converters;
    }

    @Override
    public void addConverter(PersistableAccount2SpecificAccountConverter converter) {
        converters.put(converter.supportedRepository(), converter);
    }

    @Override
    public PersistableAccount2SpecificAccountConverter getConverter(RepositoryType type) {
        return converters.get(type);
    }

    @Override
    public boolean contains(RepositoryType type) {
        return converters.containsKey(type);
    }
}
