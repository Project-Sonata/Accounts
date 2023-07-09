package com.odeyalo.sonata.account.service.event.listener;

import com.odeyalo.sonata.account.service.event.source.ReactiveSonataEventSource;
import com.odeyalo.sonata.suite.brokers.events.SonataEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class DefaultEventSourceEventListenerNotifier extends AbstractEventSourceEventListenerNotifier {
    private final EventListenerRegistry listenerRegistry;

    public DefaultEventSourceEventListenerNotifier(ReactiveSonataEventSource eventSource, EventListenerRegistry listenerRegistry) {
        super(eventSource);
        this.listenerRegistry = listenerRegistry;
    }

    @Override
    public Mono<Void> invokeEventListeners(SonataEvent event) {
        return Mono.just(event)
                .subscribeOn(Schedulers.parallel())
                .flatMap(receivedEvent -> Flux.fromIterable(listenerRegistry.getListeners())
                        .flatMap(listener -> listener.onEvent(receivedEvent))
                        .then());
    }
}
