package com.odeyalo.sonata.account.service.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Component
public class MapEventListenerContainer implements EventListenerRegistry {
    private final List<EventListener> listeners;

    @Autowired
    public MapEventListenerContainer(List<EventListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void addListener(EventListener eventListener) {
        listeners.add(eventListener);
    }

    @Override
    public List<EventListener> getListeners() {
        return listeners;
    }

    @Override
    public void remove(EventListener listener) {
        listeners.remove(listener);
    }
}
