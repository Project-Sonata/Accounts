package com.odeyalo.sonata.account.controller;

import com.odeyalo.sonata.account.dto.ChangePasswordDto;
import com.odeyalo.sonata.account.service.password.PasswordContainer;
import com.odeyalo.sonata.account.service.password.PasswordUpdatingResult;
import com.odeyalo.sonata.account.service.password.SecurePasswordUpdater;
import com.odeyalo.sonata.account.web.security.auth.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/account/settings/")
public class AccountSettingsController {

    private final SecurePasswordUpdater updater;

    public AccountSettingsController(SecurePasswordUpdater updater) {
        this.updater = updater;
    }

    @PutMapping(value = "/change-password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<?>> changePassword(@RequestBody ChangePasswordDto body) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(authentication -> (AuthenticatedUser) authentication)
                .flatMap(user -> updater.updatePassword(user, PasswordContainer.of(body.getOldPassword(), body.getNewPassword())))
                .map(result ->
                        ResponseEntity.status(result.isUpdated() ? OK : BAD_REQUEST)
                                .body(PasswordUpdatingResult.of(result.isUpdated(), result.getErrorDetails()))
                );
    }
}
