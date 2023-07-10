package com.odeyalo.sonata.account;

import com.odeyalo.sonata.account.entity.Gender;
import com.odeyalo.sonata.account.repository.storage.PersistableAccount;
import com.odeyalo.sonata.account.repository.storage.ReactiveAccountStorage;
import com.odeyalo.sonata.suite.reactive.annotation.EnableSuiteReactive;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.Month;

@SpringBootApplication
@EnableDiscoveryClient
@EnableSuiteReactive
public class AccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountApplication.class, args);
    }


    @Bean
    public ApplicationRunner runner(ReactiveAccountStorage storage) {
        return (args) -> {
            PersistableAccount account = PersistableAccount.builder()
                    .id("1")
                    .creationTime(System.currentTimeMillis())
                    .birthdate(LocalDate.of(2000, 10, 12))
                    .email("odeyalo@gmail.com")
                    .countryCode("JP")
                    .username("xsafswfasf")
                    .gender(Gender.MALE)
                    .build();

            storage.save(account).block();

            PersistableAccount account2 = PersistableAccount.builder()
                    .id("2")
                    .creationTime(System.currentTimeMillis())
                    .birthdate(LocalDate.of(2000, Month.MAY, 5))
                    .email("miku@gmail.com")
                    .countryCode("JP")
                    .username("mikuuuu")
                    .gender(Gender.FEMALE)
                    .build();
            storage.save(account2).block();
        };
    }
}