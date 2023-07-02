package com.odeyalo.sonata.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountInformationDto {
    @JsonProperty("id")
    String id;
    @JsonProperty("username")
    String username;
    @JsonProperty("email")
    String email;
    @JsonProperty("gender")
    String gender;
    @JsonProperty("birthdate")
    LocalDate birthdate;
    @JsonProperty("country")
    String countryCode;
}
