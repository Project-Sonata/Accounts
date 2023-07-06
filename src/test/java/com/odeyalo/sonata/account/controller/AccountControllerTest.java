package com.odeyalo.sonata.account.controller;

import com.odeyalo.sonata.account.SharedComponent;
import com.odeyalo.sonata.account.dto.AccountInformationDto;
import com.odeyalo.sonata.account.entity.Account;
import com.odeyalo.sonata.account.entity.Gender;
import com.odeyalo.sonata.account.repository.storage.PersistableAccount;
import com.odeyalo.sonata.account.repository.storage.ReactiveAccountStorage;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Hooks;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties.StubsMode.REMOTE;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureWebTestClient
@AutoConfigureStubRunner(stubsMode = REMOTE,
        repositoryRoot = "git://https://github.com/Project-Sonata/Sonata-Contracts.git",
        ids = "com.odeyalo.sonata:authorization:*")
@Import(SharedComponent.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class AccountControllerTest {

    public static final String CURRENT_ACCOUNT_ENDPOINT = "/account/me";

    @Autowired
    WebTestClient webClient;

    @Autowired
    ReactiveAccountStorage accountStorage;

    Account expectedAccount;

    String prefix = "Bearer ";

    @BeforeAll
    void setUp() {
        Hooks.onOperatorDebug();
        PersistableAccount account = PersistableAccount.builder()
                .id("1")
                .email("nakanomiku@gmail.com")
                .username("MikuNakano")
                .countryCode("JP")
                .gender(Gender.FEMALE)
                .creationTime(System.currentTimeMillis())
                .birthdate(LocalDate.of(2004, Month.MAY, 5))
                .build();

        expectedAccount = accountStorage.save(account).block();
    }

    //    GET /account/me - get the account info for current user.

    @Nested
    @Import(SharedComponent.class)
    class CurrentAccountEndpointWithoutAuthorization {

        @Test
        @DisplayName("expect 401 status code")
        void expect401StatusCode() {
            WebTestClient.ResponseSpec exchange = prepareRequestWithoutAuthorizationAndSend();
            exchange.expectStatus().isUnauthorized();
        }

        @Test
        @DisplayName("Expect 'application/json' content type")
        void expectApplicationJsonContentType() {
            WebTestClient.ResponseSpec exchange = prepareRequestWithoutAuthorizationAndSend();
            exchange.expectHeader().contentType(MediaType.APPLICATION_JSON);
        }

        private WebTestClient.ResponseSpec prepareRequestWithoutAuthorizationAndSend() {
            return webClient.get()
                    .uri(CURRENT_ACCOUNT_ENDPOINT)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .exchange();
        }
    }

    @Nested
    @Import(SharedComponent.class)
    class CurrentAccountEndpointWithInvalidToken {
        String invalidTokenValue = "invalid_token";

        @Test
        void expect401() {
            WebTestClient.ResponseSpec responseSpec = prepareRequestWithInvalidAuthorizationAndSend();
            responseSpec.expectStatus().isUnauthorized();
        }

        @Test
        void expectApplicationJson() {
            WebTestClient.ResponseSpec responseSpec = prepareRequestWithInvalidAuthorizationAndSend();
            responseSpec.expectHeader().contentType(MediaType.APPLICATION_JSON);
        }

        private WebTestClient.ResponseSpec prepareRequestWithInvalidAuthorizationAndSend() {
            return webClient.get()
                    .uri(CURRENT_ACCOUNT_ENDPOINT)
                    .header(HttpHeaders.AUTHORIZATION, invalidTokenValue)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .exchange();
        }
    }

    @Nested
    @Import(SharedComponent.class)
    class CurrentAccountEndpointWithValidAuthorization {
        String authorizationHeaderValue = prefix + "mikunakanoisthebestgirl";

        @Test
        @DisplayName("Expect 200 OK status code")
        void expectOKStatusCode() {
            WebTestClient.ResponseSpec exchange = prepareRequestWithAuthorizationHeaderAndSend();
            exchange.expectStatus().isOk();
        }

        @Test
        @DisplayName("Expect 'application/json' content type")
        void expectApplicationJsonContentType() {
            WebTestClient.ResponseSpec exchange = prepareRequestWithAuthorizationHeaderAndSend();
            exchange.expectHeader().contentType(MediaType.APPLICATION_JSON);
        }

        @Test
        @DisplayName("Expect parsable body in response")
        void expectParsableBody() {
            WebTestClient.ResponseSpec exchange = prepareRequestWithAuthorizationHeaderAndSend();
            AccountInformationDto body = exchange.expectBody(AccountInformationDto.class)
                    .returnResult().getResponseBody();
            assertNotNull(body);
        }

        @Test
        @DisplayName("Expect account id associated with token in authorization header")
        void expectAccountInfoIDAssociatedWithToken() {
            WebTestClient.ResponseSpec exchange = prepareRequestWithAuthorizationHeaderAndSend();
            AccountInformationDto body = exchange.expectBody(AccountInformationDto.class).returnResult().getResponseBody();
            assertEquals(expectedAccount.getId(), body.getId(), "User id must be the same as in the token!");
        }

        @Test
        @DisplayName("Expect account username associated with token in authorization header")
        void expectAccountInfoUsernameAssociatedWithToken() {
            WebTestClient.ResponseSpec exchange = prepareRequestWithAuthorizationHeaderAndSend();
            AccountInformationDto body = exchange.expectBody(AccountInformationDto.class).returnResult().getResponseBody();
            assertEquals(expectedAccount.getUsername(), body.getUsername(), "Username must be associated with token!!");
        }

        @Test
        @DisplayName("Expect account email associated with token in authorization header")
        void expectAccountInfoEmailAssociatedWithToken() {
            WebTestClient.ResponseSpec exchange = prepareRequestWithAuthorizationHeaderAndSend();
            AccountInformationDto body = exchange.expectBody(AccountInformationDto.class).returnResult().getResponseBody();
            assertEquals(expectedAccount.getEmail(), body.getEmail(), "Email must be the associated with the token!");
        }

        @Test
        @DisplayName("Expect user's account birthdate associated with token in authorization header")
        void expectAccountInfoWithBirthdateAssociatedWithToken() {
            WebTestClient.ResponseSpec exchange = prepareRequestWithAuthorizationHeaderAndSend();
            AccountInformationDto body = exchange.expectBody(AccountInformationDto.class).returnResult().getResponseBody();
            assertEquals(expectedAccount.getBirthdate(), body.getBirthdate(), "Birthdate must be correct!");
        }

        @Test
        @DisplayName("Expect user's account country code associated with user id in token")
        void expectAccountInfoWithCountryCodeAssociatedWithToken() {
            WebTestClient.ResponseSpec exchange = prepareRequestWithAuthorizationHeaderAndSend();
            AccountInformationDto body = exchange.expectBody(AccountInformationDto.class).returnResult().getResponseBody();
            assertEquals(expectedAccount.getCountryCode(), body.getCountryCode(), "Birthdate must be correct!");
        }

        @Test
        @DisplayName("Expect user's gender associated with user id in token")
        void expectAccountInfoWithValidGenderCodeAssociatedWithToken() {
            WebTestClient.ResponseSpec exchange = prepareRequestWithAuthorizationHeaderAndSend();
            AccountInformationDto body = exchange.expectBody(AccountInformationDto.class).returnResult().getResponseBody();
            assertEquals(expectedAccount.getGender().toString(), body.getGender(), "Birthdate must be correct!");
        }

        private WebTestClient.ResponseSpec prepareRequestWithAuthorizationHeaderAndSend() {
            return webClient.get()
                    .uri(CURRENT_ACCOUNT_ENDPOINT)
                    .header(HttpHeaders.AUTHORIZATION, authorizationHeaderValue)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .exchange();
        }
    }
}

