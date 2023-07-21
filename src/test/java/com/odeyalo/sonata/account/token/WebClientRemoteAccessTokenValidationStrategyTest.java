package com.odeyalo.sonata.account.token;

import com.odeyalo.sonata.account.support.token.WebClientRemoteAccessTokenValidationStrategy;
import com.odeyalo.sonata.common.authorization.TokenIntrospectionResponse;
import com.odeyalo.suite.security.auth.token.ValidatedAccessToken;
import com.odeyalo.suite.security.auth.token.converter.TokenIntrospectionResponse2ValidatedAccessTokenConverter;
import com.odeyalo.suite.security.auth.token.converter.ValidatedAccessTokenConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties.StubsMode.REMOTE;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(WebClientRemoteAccessTokenValidationStrategyTestConfiguration.class)
@AutoConfigureStubRunner(stubsMode = REMOTE,
        repositoryRoot = "git://https://github.com/Project-Sonata/Sonata-Contracts.git",
        ids = "com.odeyalo.sonata:authorization:+:stubs:${stubrunner.runningstubs.authorization.port}")
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureWebClient
@ExtendWith(SpringExtension.class)
class WebClientRemoteAccessTokenValidationStrategyTest {


    @Autowired
    WebClientRemoteAccessTokenValidationStrategy validationStrategy;

    String validAccessToken = "mikunakanoisthebestgirl";
    String[] predefinedScopes = {"user-account-modify", "write"};

    @Test
    void validateValidAccessToken_andExpectNotNull() {
        ValidatedAccessToken result = validationStrategy.validateAccessToken(validAccessToken).block();
        assertNotNull(result, "Result must be not nulL!");
    }

    @Test
    void validateValidAccessToken_andExpectValidTrue() {
        ValidatedAccessToken result = validationStrategy.validateAccessToken(validAccessToken).block();
        assertTrue(result.isValid(), "If token is valid, then true must be returned!");
    }

    @Test
    void validateValidAccessToken_andExpectNotNullToken() {
        ValidatedAccessToken result = validationStrategy.validateAccessToken(validAccessToken).block();
        assertNotNull(result.getToken(), "The token metadata must be not null!");
    }

    @Test
    void validateValidAccessToken_andExpectScopesInMedata() {
        ValidatedAccessToken result = validationStrategy.validateAccessToken(validAccessToken).block();
        assertTrue(List.of(predefinedScopes).containsAll(List.of(result.getToken().getScopes())));;
    }

    @Test
    void validateInvalidAccessToken_andExpectNull() {
        ValidatedAccessToken token = validationStrategy.validateAccessToken("invalidtoken").block();
        assertEquals(ValidatedAccessToken.invalid(), token, "Token must be equal to invalid one!");
    }
}

@TestConfiguration
class WebClientRemoteAccessTokenValidationStrategyTestConfiguration {

    @Bean
    public ValidatedAccessTokenConverter<TokenIntrospectionResponse> validatedAccessTokenConverter() {
        return new TokenIntrospectionResponse2ValidatedAccessTokenConverter();
    }

    @Bean
    public WebClientRemoteAccessTokenValidationStrategy webClientRemoteAccessTokenValidationStrategy(
            @Qualifier("tokenValidationWebClient") WebClient tokenValidationWebClient,
            ValidatedAccessTokenConverter<TokenIntrospectionResponse> accessTokenConverter) {
        return new WebClientRemoteAccessTokenValidationStrategy(tokenValidationWebClient, accessTokenConverter);
    }

    @Bean
    public WebClient tokenValidationWebClient(@Value("${sonata.token.validation.remote.webclient.url}") String url, WebClient.Builder builder) {
        return builder
                .baseUrl(url)
                .build();
    }
}
