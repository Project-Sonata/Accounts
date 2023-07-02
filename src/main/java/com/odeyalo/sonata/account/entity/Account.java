package com.odeyalo.sonata.account.entity;

import java.time.LocalDate;

/**
 * Representation of account entity
 */
public interface Account {

    String getId();

    void setId(String id);

    String getUsername();

    void setUsername(String username);

    String getEmail();

    void setEmail(String email);

    LocalDate getBirthdate();

    void setBirthdate(LocalDate birthdate);

    Gender getGender();

    void setGender(Gender gender);

    long getCreationTime();

    void setCreationTime(long creationTime);

    String getCountryCode();

    void setCountryCode(String code);
}
