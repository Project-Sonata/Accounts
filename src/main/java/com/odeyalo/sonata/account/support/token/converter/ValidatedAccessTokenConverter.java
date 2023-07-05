package com.odeyalo.sonata.account.support.token.converter;

import com.odeyalo.sonata.account.support.token.ValidatedAccessToken;

/**
 * Convert the class to {@link ValidatedAccessToken}
 * @param <T> - type to convert from
 */
public interface ValidatedAccessTokenConverter<T> {
    ValidatedAccessToken convertTo(T from);
}
