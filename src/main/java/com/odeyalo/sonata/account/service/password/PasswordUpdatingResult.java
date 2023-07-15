package com.odeyalo.sonata.account.service.password;

import com.odeyalo.sonata.common.shared.ErrorDetails;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "of")
public class PasswordUpdatingResult {
    boolean updated;
    ErrorDetails errorDetails;

    public static PasswordUpdatingResult updated() {
        return of(true, null);
    }

    public static PasswordUpdatingResult failed(ErrorDetails details) {
        return of(false, details);
    }
}
