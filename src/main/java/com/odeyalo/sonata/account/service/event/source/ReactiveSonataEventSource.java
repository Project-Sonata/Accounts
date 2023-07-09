package com.odeyalo.sonata.account.service.event.source;

import com.odeyalo.sonata.suite.brokers.events.SonataEvent;
import reactor.core.publisher.Flux;

/**
 * Base interface that provides source of Sonata Events in reactive manner
 */
public interface ReactiveSonataEventSource {

    Flux<? extends SonataEvent> getEvents();

}
