package com.odeyalo.sonata.account.support.token;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Hooks;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties.StubsMode.REMOTE;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureStubRunner(stubsMode = REMOTE,
        repositoryRoot = "git://https://github.com/Project-Sonata/Sonata-Contracts.git",
        ids = "com.odeyalo.sonata:authorization:+:stubs:${stubrunner.runningstubs.authorization.port}")
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
class SuiteClientRemoteAccessTokenValidationStrategyTest {

    @Autowired
    SuiteClientRemoteAccessTokenValidationStrategy validationStrategy;

    public static final String VALID_ACCESS_TOKEN = "mikunakanoisthebestgirl";

    String[] predefinedScopes = {"read", "write"};

    @BeforeAll
    void setup() {
        Hooks.onOperatorDebug();
    }

    @Test
    void validateValidAccessToken_andExpectNotNull() {
        ValidatedAccessToken result = validationStrategy.validateAccessToken(VALID_ACCESS_TOKEN).block();
        assertNotNull(result, "Result must be not nulL!");
    }

    @Test
    void validateValidAccessToken_andExpectValidTrue() {
        ValidatedAccessToken result = validationStrategy.validateAccessToken(VALID_ACCESS_TOKEN).block();
        assertTrue(result.isValid(), "If token is valid, then true must be returned!");
    }

    @Test
    void validateValidAccessToken_andExpectNotNullToken() {
        ValidatedAccessToken result = validationStrategy.validateAccessToken(VALID_ACCESS_TOKEN).block();
        assertNotNull(result.getToken(), "The token metadata must be not null!");
    }

    @Test
    void validateValidAccessToken_andExpectScopesInMedata() {
        ValidatedAccessToken result = validationStrategy.validateAccessToken(VALID_ACCESS_TOKEN).block();
        assertArrayEquals(predefinedScopes, result.getToken().getScopes());
    }

    @Test
    void validateInvalidAccessToken_andExpectNull() {
        ValidatedAccessToken token = validationStrategy.validateAccessToken("invalidtoken").block();
        assertEquals(ValidatedAccessToken.invalid(), token, "Token must be equal to invalid one!");
    }
}