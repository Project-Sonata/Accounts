package com.odeyalo.sonata.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.odeyalo.sonata.common.shared.ErrorDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangingResultDto {
    @JsonProperty("updated")
    private boolean updated;
    private ErrorDetails errorDetails;

    public static PasswordChangingResultDto updated() {
        return new PasswordChangingResultDto(true, null);
    }

    public static PasswordChangingResultDto failed(ErrorDetails errorDetails) {
        return new PasswordChangingResultDto(false, errorDetails);
    }
}
