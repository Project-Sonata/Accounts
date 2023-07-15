package com.odeyalo.sonata.account.configuration;

import com.odeyalo.sonata.account.web.interceptor.RequestInterceptor;
import com.odeyalo.sonata.suite.reactive.config.support.interceptor.SuiteReactiveHttpRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.odeyalo.sonata.account.web.interceptor.SuiteReactiveHttpRequestInterceptorAdapter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Registry the interceptors in different beans, configs, etc
 */
@Configuration
public class SuiteInterceptorsRegistrarConfiguration {

    @Bean
    public List<SuiteReactiveHttpRequestInterceptor> suiteReactiveHttpRequestInterceptors(List<RequestInterceptor> interceptors) {
        return interceptors.stream()
                .map(SuiteReactiveHttpRequestInterceptorAdapter::new)
                .collect(Collectors.toList());
    }
}
