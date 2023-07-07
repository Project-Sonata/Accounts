package com.odeyalo.sonata.account.web.security.auth;

import com.odeyalo.sonata.account.exception.InvalidAccessTokenException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties.StubsMode.REMOTE;

/**
 * Integration test for {@link TokenAuthenticationManager}
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureStubRunner(stubsMode = REMOTE,
        repositoryRoot = "git://https://github.com/Project-Sonata/Sonata-Contracts.git",
        ids = "com.odeyalo.sonata:authorization:+:stubs")
@TestPropertySource(locations = "classpath:application-test.properties")
class TokenAuthenticationManagerTest {


    public static final String INVALID_TOKEN = "invalidtoken";
    public static final String VALID_ACCESS_TOKEN = "mikunakanoisthebestgirl";

    @Autowired
    TokenAuthenticationManager tokenAuthenticationManager;

    List<String> predefinedScopes = List.of("user-account-modify", "write");

    @BeforeAll
    void hardCodedTestsPass() {
        Hooks.onOperatorDebug();
    }

    @Test
    void authenticateWithValidToken_AndExpectTrueInAuthenticated() {
        Authentication authentication = prepareValidTokenAndGetResult("Miku");
        assertTrue(authentication::isAuthenticated, "If token is valid, then user must be authenticated!");
    }

    @Test
    void authenticateWithValidToken_AndExpectTheSameScopesInResultAsAuthorizationServerRespond() {
        Authentication authentication = prepareValidTokenAndGetResult("Miku");
        List<String> authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        assertEquals(predefinedScopes, authorities, "The scopes must be the same as remote server respond");
    }

    @Test
    void authenticateWithValidToken_AndExpectTheSameUsernameAsWasProvided() {
        Authentication authentication = prepareValidTokenAndGetResult("Odeyalo");
        assertEquals("Odeyalo", authentication.getPrincipal());
    }

    @Test
    void authenticateWithNotValidAuthenticationImpl_andExpectEmptyMono() {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("principal", "credentials");
        Mono<Authentication> result = tokenAuthenticationManager.authenticate(authenticationToken);
        StepVerifier.create(result)
                .expectComplete()
                .verify();
    }

    @Test
    void authenticationWithInvalidToken_AndExpectError() throws Exception {
        TokenAuthentication authenticationToken = new TokenAuthentication("Masiro", INVALID_TOKEN);
        Mono<Authentication> result = tokenAuthenticationManager.authenticate(authenticationToken);
        StepVerifier.create(result)
                .expectError(InvalidAccessTokenException.class)
                .verify();
    }

    private Authentication prepareValidTokenAndGetResult(String username) {
        TokenAuthentication authenticationToken = new TokenAuthentication(username, VALID_ACCESS_TOKEN);
        return tokenAuthenticationManager.authenticate(authenticationToken).block();
    }
}