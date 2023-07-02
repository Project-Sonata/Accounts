package com.odeyalo.sonata.account.repository;

import com.odeyalo.sonata.account.entity.Gender;
import com.odeyalo.sonata.account.entity.InMemoryAccount;
import com.odeyalo.sonata.account.repository.support.AtomicLongIdGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReactiveInMemoryAccountRepositoryTest {
    InMemoryAccount existingAccount = InMemoryAccount.builder()
            .id("1")
            .username("odeyalo")
            .email("odeyalo@gmail")
            .gender(Gender.MALE)
            .birthdate(LocalDate.of(2001, Month.MARCH,3))
            .countryCode("UA")
            .build();

    ReactiveInMemoryAccountRepository repository;

    @BeforeEach
    void setup() {
        repository = new ReactiveInMemoryAccountRepository(
                List.of(existingAccount), new AtomicLongIdGenerator()
        );
    }

    @AfterEach
    void cleanup() {
        repository.clear().block();
    }

    @Test
    @DisplayName("Find non existing account by email and expect null")
    void findNonExistingAccountByEmail() {
        InMemoryAccount account = repository.findAccountByEmail("not exist").block();
        assertNull(account);
    }

    @Test
    @DisplayName("Find existing account by email and expect account")
    void findExistingAccountByEmail_andExpectAccount() {
        InMemoryAccount account = repository.findAccountByEmail(existingAccount.getEmail()).block();
        assertEquals(existingAccount, account, "Accounts must be equal!");
    }

    @Test
    @DisplayName("Find account by not existing  username and expect null")
    void findAccountByNotExistingUsername_andExpectNull() {
        InMemoryAccount account = repository.findAccountByUsername("username12").block();
        assertNull(account);
    }

    @Test
    @DisplayName("Find account by  username and expect account")
    void findAccountByExistingUsername_andExpectAccount() {
        InMemoryAccount account = repository.findAccountByUsername(existingAccount.getUsername()).block();
        assertEquals(existingAccount, account, "Accounts must be equal");
    }

    @Test
    @DisplayName("Save account and expect id to be generated")
    void saveAccount_andExpectIdToBeGenerated() {
        InMemoryAccount account = InMemoryAccount.builder()
                .username("Miku")
                .email("makano@gmail")
                .gender(Gender.FEMALE)
                .birthdate(LocalDate.of(2002, Month.MAY, 5))
                .countryCode("JP")
                .build();

        InMemoryAccount saved = repository.save(account).block();

        assertNotNull(saved.getId(), "Id must be generated!");
    }

    @Test
    @DisplayName("Save account and expect account to be saved")
    void saveAccount_andExpectAccountToBeSaved() {
        InMemoryAccount account = InMemoryAccount.builder()
                .username("Miku")
                .email("makano@gmail")
                .gender(Gender.FEMALE)
                .birthdate(LocalDate.of(2002, Month.MAY, 5))
                .countryCode("JP")
                .build();

        InMemoryAccount saved = repository.save(account).block();

        InMemoryAccount found = repository.findById(saved.getId()).block();

        assertEquals(saved, found);
    }


    @Test
    void findById() {
        InMemoryAccount account = repository.findById(existingAccount.getId()).block();
        assertEquals(existingAccount, account);
    }

    @Test
    void deleteById() {
        repository.deleteById(existingAccount.getId()).block();

        InMemoryAccount account = repository.findById(existingAccount.getId()).block();
        assertNull(account, "if account was deleted, then null must be returned!");
    }

    @Test
    void getRepositoryType() {
        assertEquals(RepositoryType.IN_MEMORY, repository.getRepositoryType());
    }
}