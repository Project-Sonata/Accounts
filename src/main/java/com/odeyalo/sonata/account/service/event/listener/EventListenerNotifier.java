package com.odeyalo.sonata.account.service.event.listener;

import com.odeyalo.sonata.account.service.event.source.ReactiveSonataEventSource;
import com.odeyalo.sonata.suite.brokers.events.SonataEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public interface EventListenerNotifier {

    Mono<Void> invokeEventListeners(SonataEvent event);

}
