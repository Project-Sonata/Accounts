package com.odeyalo.sonata.account.service.event.listener;

import com.odeyalo.sonata.suite.brokers.events.SonataEvent;
import reactor.core.publisher.Mono;

public interface EventListener {

    Mono<Void> onEvent(SonataEvent event);

}
