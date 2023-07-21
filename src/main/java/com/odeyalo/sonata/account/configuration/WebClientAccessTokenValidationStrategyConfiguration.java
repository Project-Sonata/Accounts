package com.odeyalo.sonata.account.configuration;

import com.odeyalo.sonata.account.support.token.WebClientRemoteAccessTokenValidationStrategy;
import com.odeyalo.sonata.common.authorization.TokenIntrospectionResponse;
import com.odeyalo.suite.security.auth.token.converter.ValidatedAccessTokenConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ConditionalOnMissingClass("com.odeyalo.sonata.suite.reactive.SuiteReactiveApplication")
public class WebClientAccessTokenValidationStrategyConfiguration implements InitializingBean {
    private final Logger logger = LoggerFactory.getLogger("WebClientValidationStrategyConfiguration");

    @Bean
    public WebClientRemoteAccessTokenValidationStrategy webClientRemoteAccessTokenValidationStrategy(
            @Qualifier("tokenValidationWebClient") WebClient tokenValidationWebClient,
            ValidatedAccessTokenConverter<TokenIntrospectionResponse> accessTokenConverter) {
        return new WebClientRemoteAccessTokenValidationStrategy(tokenValidationWebClient, accessTokenConverter);
    }

    @Bean
    public WebClient tokenValidationWebClient(@Value("${sonata.token.validation.remote.webclient.url}") String url, WebClient.Builder builder) {
        logger.info("Using the [ {} ] URL to construct the token validation web client", url);
        return builder
                .baseUrl(url)
                .build();
    }

    @Override
    public void afterPropertiesSet() {
        this.logger.warn("WebClientRemoteAccessTokenValidationStrategy is used as the strategy to validate the access tokens\n" +
                "While this implementation can be used in development there is a recommendation to use Suite-Reactive package to communicate between Sonata-Microservices");
    }
}