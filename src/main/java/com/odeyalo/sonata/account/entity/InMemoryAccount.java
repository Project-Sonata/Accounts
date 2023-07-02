package com.odeyalo.sonata.account.entity;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InMemoryAccount implements Account {
    private String id;
    private String username;
    private String email;
    private LocalDate birthdate;
    private Gender gender;
    private long creationTime;
    private String countryCode;
}
