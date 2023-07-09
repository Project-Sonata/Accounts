package com.odeyalo.sonata.account.service.event.listener;

import com.odeyalo.sonata.account.service.event.handler.UserRegisteredEventHandler;
import com.odeyalo.sonata.suite.brokers.events.SonataEvent;
import com.odeyalo.sonata.suite.brokers.events.user.UserRegisteredEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserRegisteredEventListener implements EventListener {
    private final UserRegisteredEventHandler eventHandler;

    @Autowired
    public UserRegisteredEventListener(UserRegisteredEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @Override
    public Mono<Void> onEvent(SonataEvent event) {
        if (!(event instanceof UserRegisteredEvent userRegisteredEvent)) {
            return Mono.empty();
        }
        return eventHandler.handleEvent(userRegisteredEvent);
    }
}