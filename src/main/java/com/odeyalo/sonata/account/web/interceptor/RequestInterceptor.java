package com.odeyalo.sonata.account.web.interceptor;

/**
 * Intercept the request before request being sent
 */
public interface RequestInterceptor {
    void apply(CustomizableRequest request);
}