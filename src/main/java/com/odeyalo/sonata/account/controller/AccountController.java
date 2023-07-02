package com.odeyalo.sonata.account.controller;

import com.odeyalo.sonata.account.dto.AccountInformationDto;
import com.odeyalo.sonata.account.repository.storage.ReactiveAccountStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private ReactiveAccountStorage accountStorage;

    @GetMapping(value = "/me", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<AccountInformationDto> getCurrentAccountInfo() {
        return accountStorage.findById("1")
                .map(account -> AccountInformationDto.builder()
                        .id(account.getId())
                        .username(account.getUsername())
                        .email(account.getEmail())
                        .gender(account.getGender().toString())
                        .birthdate(account.getBirthdate())
                        .countryCode(account.getCountryCode())
                        .build());
    }

}