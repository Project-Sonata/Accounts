package com.odeyalo.sonata.account.web.interceptor;

import io.netty.handler.codec.http.cookie.Cookie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * Simple implementation of {@link CustomizableRequest} that just stores values as container
 */
@Value
@Builder(toBuilder = true)
@AllArgsConstructor(staticName = "of")
public class SimpleCustomizableRequest implements CustomizableRequest {
    MultiValueMap<String, String> headers;
    Map<String, String> cookies;
    String requestBody;

    @Override
    public void addHeader(String key, String value) {
        headers.add(key, value);
    }

    @Override
    public void removeHeader(String key) {
        headers.remove(key);
    }

    @Override
    public boolean containsHeader(String header) {
        return headers.containsKey(header);
    }

    @Override
    public void addCookie(String key, String value) {
        cookies.put(key, value);
    }

    @Override
    public void addCookie(Cookie cookie) {
        cookies.put(cookie.name(), cookie.value());
    }

    @Override
    public boolean containsCookie(String cookie) {
        return cookies.containsKey(cookie);
    }

    @Override
    public void removeCookie(String key) {
        cookies.remove(key);
    }
}
