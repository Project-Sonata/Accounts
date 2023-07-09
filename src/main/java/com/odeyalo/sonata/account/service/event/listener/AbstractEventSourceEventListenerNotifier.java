package com.odeyalo.sonata.account.service.event.listener;

import com.odeyalo.sonata.account.service.event.source.ReactiveSonataEventSource;
import com.odeyalo.sonata.suite.brokers.events.SonataEvent;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public abstract class AbstractEventSourceEventListenerNotifier implements EventListenerNotifier {
    protected final ReactiveSonataEventSource eventSource;
    private Disposable subscriptionStatus;

    protected AbstractEventSourceEventListenerNotifier(ReactiveSonataEventSource eventSource) {
        this.eventSource = eventSource;
    }

    public Disposable start() {
        if (subscriptionStatus != null && subscriptionStatus.isDisposed()) {
            throw new IllegalStateException("Subscription has been already closed!");
        }
        Disposable disposable = consumeEventsAndDelegate();
        this.subscriptionStatus = disposable;
        return disposable;
    }

    public void close() {
        subscriptionStatus.dispose();
    }


    private Disposable consumeEventsAndDelegate() {
        return eventSource.getEvents()
                .subscribeOn(Schedulers.parallel())
                .flatMap(this::invokeEventListeners)
                .subscribe();
    }
}
