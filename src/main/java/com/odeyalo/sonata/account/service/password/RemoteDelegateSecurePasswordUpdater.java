package com.odeyalo.sonata.account.service.password;

import com.odeyalo.sonata.account.web.security.auth.AuthenticatedUser;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class RemoteDelegateSecurePasswordUpdater implements SecurePasswordUpdater {
    private final RemotePasswordUpdaterStrategy updaterStrategy;

    public RemoteDelegateSecurePasswordUpdater(RemotePasswordUpdaterStrategy updaterStrategy) {
        this.updaterStrategy = updaterStrategy;
    }

    @Override
    public Mono<PasswordUpdatingResult> updatePassword(AuthenticatedUser user, PasswordContainer container) {
        return updaterStrategy.updatePassword(user, container);
    }
}
