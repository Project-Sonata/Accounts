package com.odeyalo.sonata.account.configuration;

import com.odeyalo.sonata.account.service.event.listener.AbstractEventSourceEventListenerNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class EventConsumingStarter {
    private final Logger logger = LoggerFactory.getLogger(EventConsumingStarter.class);

    @EventListener(ApplicationStartedEvent.class)
    public void startEventConsuming(ApplicationStartedEvent event) {
        AbstractEventSourceEventListenerNotifier notifier = event.getApplicationContext().getBean(AbstractEventSourceEventListenerNotifier.class);
        notifier.start();
        this.logger.info("Started to consume events from: {}", notifier);
    }
}
