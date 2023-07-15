package com.odeyalo.sonata.account.web.interceptor;

import io.netty.handler.codec.http.cookie.Cookie;
import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * Represent the request that can be customized, it useful when request needs to be customized before being sent.
 *
 * In most cases used as wrapper for third-party libraries
 * @see ReactiveFeignCustomizableRequest
 */
public interface CustomizableRequest {
    void addHeader(String key, String value);

    void removeHeader(String key);

    boolean containsHeader(String header);

    void addCookie(String key, String value);

    void addCookie(Cookie cookie);

    boolean containsCookie(String cookie);

    void removeCookie(String key);

    MultiValueMap<String, String> getHeaders();

    Map<String, String> getCookies();

    Object getRequestBody();
}
