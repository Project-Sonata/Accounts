package com.odeyalo.sonata.account.service.event.listener;

import java.util.List;

public interface EventListenerRegistry {

    void addListener(EventListener eventListener);

    List<EventListener> getListeners();

    void remove(EventListener listener);
}
