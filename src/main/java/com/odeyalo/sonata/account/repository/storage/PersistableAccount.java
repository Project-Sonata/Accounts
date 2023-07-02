package com.odeyalo.sonata.account.repository.storage;

import com.odeyalo.sonata.account.entity.Account;
import com.odeyalo.sonata.account.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * High level account entity impl, that does not depend on specific repository
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersistableAccount implements Account {
    private String id;
    private String username;
    private String email;
    private LocalDate birthdate;
    private Gender gender;
    private long creationTime;
    private String countryCode;
}
