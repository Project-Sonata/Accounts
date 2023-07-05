package com.odeyalo.sonata.account.support.token.converter;

import com.odeyalo.sonata.account.support.token.AccessTokenMetadata;
import com.odeyalo.sonata.account.support.token.ValidatedAccessToken;
import com.odeyalo.sonata.common.authorization.TokenIntrospectionResponse;
import org.springframework.stereotype.Component;

/**
 * Convert the {@link TokenIntrospectionResponse} to {@link ValidatedAccessToken}
 */
@Component
public class TokenIntrospectionResponse2ValidatedAccessTokenConverter implements ValidatedAccessTokenConverter<TokenIntrospectionResponse> {

    @Override
    public ValidatedAccessToken convertTo(TokenIntrospectionResponse body) {
        if (!body.isValid()) {
            return ValidatedAccessToken.invalid();
        }
        AccessTokenMetadata metadata = AccessTokenMetadata.of(body.getUserId(), body.getScope().split(" "),
                body.getIssuedAt(), body.getExpiresIn());

        return ValidatedAccessToken.valid(metadata);
    }
}