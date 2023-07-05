package com.odeyalo.sonata.account.web.security.auth;

import com.odeyalo.sonata.account.SharedComponent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerPort;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.TestPropertySource;

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
        ids = "com.odeyalo.sonata:authorization:+:stubs:${stubrunner.runningstubs.authorization.port}")
@Import(SharedComponent.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class TokenAuthenticationManagerTest {

    @Autowired
    TokenAuthenticationManager tokenAuthenticationManager;

    String validAccessToken = "mikunakanoisthebestgirl";
    List<String> predefinedScopes = List.of("read", "write");

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

    private Authentication prepareValidTokenAndGetResult(String username) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, validAccessToken);
        return tokenAuthenticationManager.authenticate(authenticationToken).block();
    }

    @Test
    void authenticationWithInvalidToken_AndExpectNull() throws Exception {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("Masiro", "invalid_token");
        Authentication result = tokenAuthenticationManager.authenticate(authenticationToken).block();
        assertNull(result, "If token is invalid, then null must be returned");
    }
}