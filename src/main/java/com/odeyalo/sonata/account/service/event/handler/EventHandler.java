package com.odeyalo.sonata.account.service.event.handler;

import com.odeyalo.sonata.suite.brokers.events.SonataEvent;
import reactor.core.publisher.Mono;

public interface EventHandler {

    Mono<Void> handleEvent(SonataEvent event);

}
