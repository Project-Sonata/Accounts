package com.odeyalo.sonata.account.service.event.handler;

import com.odeyalo.sonata.suite.brokers.events.SonataEvent;
import com.odeyalo.sonata.suite.brokers.events.user.UserRegisteredEvent;
import reactor.core.publisher.Mono;

public interface UserRegisteredEventHandler extends EventHandler {
    @Override
    default Mono<Void> handleEvent(SonataEvent event) {
        if (!(event instanceof UserRegisteredEvent userRegisteredEvent)) {
            return Mono.empty();
        }
        return handleEvent(userRegisteredEvent);
    }

    Mono<Void> handleEvent(UserRegisteredEvent event);

}
