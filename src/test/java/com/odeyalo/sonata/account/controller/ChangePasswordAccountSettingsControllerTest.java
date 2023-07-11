package com.odeyalo.sonata.account.controller;

import com.odeyalo.sonata.account.SharedComponent;
import com.odeyalo.sonata.account.dto.ChangePasswordDto;
import com.odeyalo.sonata.account.dto.PasswordChangingResultDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Hooks;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties.StubsMode.REMOTE;

@AutoConfigureStubRunner(stubsMode = REMOTE,
        repositoryRoot = "git://https://github.com/Project-Sonata/Sonata-Contracts.git",
        ids = "com.odeyalo.sonata:authorization:+")
@Import(SharedComponent.class)
public class ChangePasswordAccountSettingsControllerTest extends AccountSettingsControllerTest {

    @Autowired
    WebTestClient webTestClient;

    String CHANGE_PASSWORD_URI = "/account/settings/change-password";

    public static final String VALID_ACCESS_TOKEN = "mikunakanoisthebestgirl";
    public static final String INVALID_ACCESS_TOKEN = "invalidtoken";
    public static final String PREFIX = "Bearer";

    @BeforeAll
    public void setup() {
        Hooks.onOperatorDebug();
    }

    @Nested
    class ChangePasswordWithValidData {

        @Test
        void expectOkStatus() {
            WebTestClient.ResponseSpec exchange = prepareAndSendValidChangePasswordRequest();
            exchange.expectStatus().isOk();
        }

        @Test
        void expectApplicationJson() {
            WebTestClient.ResponseSpec exchange = prepareAndSendValidChangePasswordRequest();
            exchange.expectHeader().contentType(MediaType.APPLICATION_JSON);
        }

        @Test
        void expectResponseBodyNotNull() {
            WebTestClient.ResponseSpec exchange = prepareAndSendValidChangePasswordRequest();
            PasswordChangingResultDto body = exchange.expectBody(PasswordChangingResultDto.class).returnResult().getResponseBody();
            assertNotNull(body);
        }

        @Test
        void expectTrueUpdatedFieldInResponseBody() {
            WebTestClient.ResponseSpec exchange = prepareAndSendValidChangePasswordRequest();
            PasswordChangingResultDto body = exchange.expectBody(PasswordChangingResultDto.class).returnResult().getResponseBody();
            assertTrue(body.isUpdated(), "Updated must be set to true if 200 OK was returned!");
        }

        WebTestClient.ResponseSpec prepareAndSendValidChangePasswordRequest() {
            ChangePasswordDto changePasswordDto = new ChangePasswordDto("old_password123", "new_password123");
            return sendChangePasswordRequest(changePasswordDto, PREFIX + " " + VALID_ACCESS_TOKEN);
        }
    }

    @Nested
    class ChangePasswordWithInvalidOldPassword  {
        String invalidOldPassword = "invalid";

        @Test
        void expectApplicationJson() {
            WebTestClient.ResponseSpec exchange = prepareAndSendRequestWithInvalidOldPassword();
            exchange.expectHeader().contentType(MediaType.APPLICATION_JSON);
        }

        @Test
        void expectBadRequestStatus() {
            WebTestClient.ResponseSpec exchange = prepareAndSendRequestWithInvalidOldPassword();
            exchange.expectStatus().isBadRequest();
        }

        @Test
        void expectNotNullBody() {
            WebTestClient.ResponseSpec exchange = prepareAndSendRequestWithInvalidOldPassword();
            PasswordChangingResultDto body = exchange.expectBody(PasswordChangingResultDto.class).returnResult().getResponseBody();
            assertNotNull(body);
        }

        @Test
        void expectFalseInResponseBody() {
            WebTestClient.ResponseSpec exchange = prepareAndSendRequestWithInvalidOldPassword();
            PasswordChangingResultDto body = exchange.expectBody(PasswordChangingResultDto.class).returnResult().getResponseBody();
            assertFalse(body.isUpdated(), "If password has been failed to update, then false must be set to updated field");
        }

        @Test
        void expectNotNullErrorDetailsInResponseBody() {
            WebTestClient.ResponseSpec exchange = prepareAndSendRequestWithInvalidOldPassword();
            PasswordChangingResultDto body = exchange.expectBody(PasswordChangingResultDto.class).returnResult().getResponseBody();
            assertNotNull(body.getErrorDetails(), "If password has been failed to update, then error details must be presented");
        }

        @Test
        void expectErrorDetailsCodeInResponseBody() {
            WebTestClient.ResponseSpec exchange = prepareAndSendRequestWithInvalidOldPassword();
            PasswordChangingResultDto body = exchange.expectBody(PasswordChangingResultDto.class).returnResult().getResponseBody();
            assertEquals(body.getErrorDetails().getCode(), "old_password_mismatch", "If old password is wrong, then code must be set to proper one");
        }


        WebTestClient.ResponseSpec prepareAndSendRequestWithInvalidOldPassword() {
            ChangePasswordDto invalidRequestBody = new ChangePasswordDto(invalidOldPassword, "new_password123");
            return sendChangePasswordRequest(invalidRequestBody, PREFIX + " " + VALID_ACCESS_TOKEN);
        }
    }

    @Nested
    class ChangePasswordWithInvalidNewPassword  {
        String invalidNewPassword = "invalid";

        @Test
        void expectApplicationJson() {
            WebTestClient.ResponseSpec exchange = prepareAndSendRequestWithInvalidOldPassword();
            exchange.expectHeader().contentType(MediaType.APPLICATION_JSON);
        }

        @Test
        void expectBadRequestStatus() {
            WebTestClient.ResponseSpec exchange = prepareAndSendRequestWithInvalidOldPassword();
            exchange.expectStatus().isBadRequest();
        }

        @Test
        void expectNotNullBody() {
            WebTestClient.ResponseSpec exchange = prepareAndSendRequestWithInvalidOldPassword();
            PasswordChangingResultDto body = exchange.expectBody(PasswordChangingResultDto.class).returnResult().getResponseBody();
            assertNotNull(body);
        }

        @Test
        void expectFalseInResponseBody() {
            WebTestClient.ResponseSpec exchange = prepareAndSendRequestWithInvalidOldPassword();
            PasswordChangingResultDto body = exchange.expectBody(PasswordChangingResultDto.class).returnResult().getResponseBody();
            assertFalse(body.isUpdated(), "If password has been failed to update, then false must be set to updated field");
        }

        @Test
        void expectNotNullErrorDetailsInResponseBody() {
            WebTestClient.ResponseSpec exchange = prepareAndSendRequestWithInvalidOldPassword();
            PasswordChangingResultDto body = exchange.expectBody(PasswordChangingResultDto.class).returnResult().getResponseBody();
            assertNotNull(body.getErrorDetails(), "If password has been failed to update, then error details must be presented");
        }

        @Test
        void expectErrorDetailsCodeInResponseBody() {
            WebTestClient.ResponseSpec exchange = prepareAndSendRequestWithInvalidOldPassword();
            PasswordChangingResultDto body = exchange.expectBody(PasswordChangingResultDto.class).returnResult().getResponseBody();
            assertEquals(body.getErrorDetails().getCode(), "invalid_password", "If new password is wrong, then code must be set to proper one");
        }


        WebTestClient.ResponseSpec prepareAndSendRequestWithInvalidOldPassword() {
            ChangePasswordDto invalidRequestBody = new ChangePasswordDto("old_password123", invalidNewPassword);
            return sendChangePasswordRequest(invalidRequestBody, PREFIX + " " + VALID_ACCESS_TOKEN);
        }
    }

    @Nested
    class ChangePasswordWithoutAccessToken {
        @Test
        void expectApplicationJson() {
            WebTestClient.ResponseSpec exchange = prepareAndSendRequestWithoutToken();
            exchange.expectHeader().contentType(MediaType.APPLICATION_JSON);
        }

        @Test
        void expect401Status() {
            WebTestClient.ResponseSpec exchange = prepareAndSendRequestWithoutToken();
            exchange.expectStatus().isUnauthorized();
        }

        WebTestClient.ResponseSpec prepareAndSendRequestWithoutToken() {
            return sendChangePasswordRequest(new ChangePasswordDto(), PREFIX + " " + INVALID_ACCESS_TOKEN);
        }
    }

    WebTestClient.ResponseSpec sendChangePasswordRequest(ChangePasswordDto dto, String accessToken) {
        return webTestClient.put()
                .uri(CHANGE_PASSWORD_URI)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange();

    }

}
