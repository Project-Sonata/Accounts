package com.odeyalo.sonata.account.web.interceptor;

import io.netty.handler.codec.http.cookie.*;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactivefeign.client.ReactiveHttpRequest;

import java.util.*;
import java.util.stream.Collectors;

@Value
@AllArgsConstructor(staticName = "of")
public class ReactiveFeignCustomizableRequest implements CustomizableRequest {
    ReactiveHttpRequest targetRequest;
    ServerCookieEncoder encoder = ServerCookieEncoder.STRICT;
    ServerCookieDecoder decoder = ServerCookieDecoder.STRICT;

    @Override
    public void addHeader(String key, String value) {
        targetRequest.headers().put(key, Collections.singletonList(value));
    }

    @Override
    public void removeHeader(String key) {
        targetRequest.headers().remove(key);
    }

    @Override
    public boolean containsHeader(String header) {
        return targetRequest.headers().containsKey(header);
    }

    @Override
    public void addCookie(String key, String value) {
        Cookie cookie = new DefaultCookie(key, value);
        addCookie(cookie);
    }

    @Override
    public void addCookie(Cookie cookie) {
        String encodedCookie = encoder.encode(cookie);
        targetRequest.headers().put(HttpHeaders.COOKIE, Collections.singletonList(encodedCookie));
    }

    @Override
    public boolean containsCookie(String target) {
        Collection<String> cookieHeader = targetRequest.headers().get(HttpHeaders.COOKIE);

        Optional<Cookie> matchingCookie = Optional.ofNullable(cookieHeader)
                .orElse(Collections.emptyList())
                .stream()
                .map(decoder::decode)
                .flatMap(Collection::stream)
                .filter(cookie -> cookie.name().equals(target))
                .findFirst();

        return matchingCookie.isPresent();
    }

    @Override
    public void removeCookie(String cookieName) {
        Collection<String> cookies = targetRequest.headers().get(HttpHeaders.COOKIE);

        List<String> resultCookies = cookies.stream()
                .map(decoder::decode)
                .flatMap(Collection::stream)
                .filter(cookie -> !cookie.name().equals(cookieName))
                .map(encoder::encode)
                .toList();

        targetRequest.headers().put(HttpHeaders.COOKIE, resultCookies);
    }

    @Override
    public MultiValueMap<String, String> getHeaders() {
        Map<String, List<String>> headers = targetRequest.headers();
        return new LinkedMultiValueMap<>(headers);
    }

    @Override
    public Map<String, String> getCookies() {
        Collection<String> cookies = targetRequest.headers().get(HttpHeaders.COOKIE);

        return cookies.stream().map(decoder::decode)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Cookie::name, Cookie::value));
    }

    @Override
    public Object getRequestBody() {
        return targetRequest.body();
    }
}
