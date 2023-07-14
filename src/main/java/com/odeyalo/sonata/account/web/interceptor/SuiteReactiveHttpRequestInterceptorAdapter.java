package com.odeyalo.sonata.account.web.interceptor;

import com.odeyalo.sonata.suite.reactive.config.support.interceptor.SuiteReactiveHttpRequestInterceptor;
import reactivefeign.client.ReactiveHttpRequest;
import reactor.core.publisher.Mono;

/**
 * Adapt the {@link RequestInterceptor} to {@link SuiteReactiveHttpRequestInterceptor}
 */
public class SuiteReactiveHttpRequestInterceptorAdapter implements SuiteReactiveHttpRequestInterceptor {
    private final RequestInterceptor interceptor;

    public SuiteReactiveHttpRequestInterceptorAdapter(RequestInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public Mono<ReactiveHttpRequest> apply(ReactiveHttpRequest reactiveHttpRequest) {
        return Mono.defer(() -> {
            interceptor.apply(ReactiveFeignCustomizableRequest.of(reactiveHttpRequest));
            return Mono.just(reactiveHttpRequest);
        });
    }
}
