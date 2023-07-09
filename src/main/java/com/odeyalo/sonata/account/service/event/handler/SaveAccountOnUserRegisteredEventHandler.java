package com.odeyalo.sonata.account.service.event.handler;

import com.odeyalo.sonata.account.entity.Gender;
import com.odeyalo.sonata.account.repository.storage.PersistableAccount;
import com.odeyalo.sonata.account.repository.storage.ReactiveAccountStorage;
import com.odeyalo.sonata.suite.brokers.events.user.UserRegisteredEvent;
import com.odeyalo.sonata.suite.brokers.events.user.data.UserRegisteredEventData;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.Month;

@Component
public class SaveAccountOnUserRegisteredEventHandler implements UserRegisteredEventHandler {
    private final ReactiveAccountStorage accountStorage;

    public SaveAccountOnUserRegisteredEventHandler(ReactiveAccountStorage accountStorage) {
        this.accountStorage = accountStorage;
    }

    @Override
    public Mono<Void> handleEvent(UserRegisteredEvent event) {
        UserRegisteredEventData registeredUser = event.getEventData();

        PersistableAccount account = PersistableAccount.builder()
                .email(registeredUser.email())
                .username(RandomStringUtils.randomAlphabetic(20))
                .birthdate(registeredUser.getBirthdate())
                .countryCode(registeredUser.getCountryCode())
                .gender(Gender.valueOf(registeredUser.getGender()))
                .id(registeredUser.id())
                .creationTime(System.currentTimeMillis()).build();

        return accountStorage.save(account)
                .checkpoint("Save the user account")
                .then();
    }
}
