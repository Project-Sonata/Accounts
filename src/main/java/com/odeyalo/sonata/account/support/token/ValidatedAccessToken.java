package com.odeyalo.sonata.account.support.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "of")
@Builder
public class ValidatedAccessToken {
    boolean valid;
    AccessTokenMetadata token;

    public static ValidatedAccessToken valid(AccessTokenMetadata metadata) {
        return of(true, metadata);
    }

    public static ValidatedAccessToken invalid() {
        return of(false, null);
    }
}