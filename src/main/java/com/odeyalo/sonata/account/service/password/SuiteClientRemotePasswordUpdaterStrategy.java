package com.odeyalo.sonata.account.service.password;

import com.odeyalo.sonata.common.authentication.dto.request.PasswordContainerDto;
import com.odeyalo.sonata.common.authentication.exception.PasswordUpdatingFailedException;
import com.odeyalo.sonata.suite.reactive.client.ReactiveAuthenticationClient;
import com.odeyalo.suite.security.auth.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Update password in remote service using Suite client
 */
@Component
public class SuiteClientRemotePasswordUpdaterStrategy implements RemotePasswordUpdaterStrategy {
    private final ReactiveAuthenticationClient client;

    @Autowired
    public SuiteClientRemotePasswordUpdaterStrategy(ReactiveAuthenticationClient client) {
        this.client = client;
    }

    @Override
    public Mono<PasswordUpdatingResult> updatePassword(AuthenticatedUser user, PasswordContainer container) {
        PasswordContainerDto requestBody = PasswordContainerDto.of(container.getOldPassword(), container.getNewPassword());
        return client.changePassword(user.getDetails().getId(), requestBody)
                .flatMap(r -> r.getBody() != null ? r.getBody() : Mono.empty())
                .map(b -> PasswordUpdatingResult.of(b.isUpdated(), b.getErrorDetails()))
                .onErrorResume(err -> err instanceof PasswordUpdatingFailedException, err -> {
                    PasswordUpdatingFailedException ex = (PasswordUpdatingFailedException) err;
                    return Mono.just(PasswordUpdatingResult.failed(ex.getErrorDetails()));
                });
    }
}
