package com.diosmc.client.events;

import java.util.*;
import java.util.function.Consumer;

public class EventBus {

    private final Map<Class<?>, List<Consumer<Object>>> listeners = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> void subscribe(Class<T> type, Consumer<T> listener) {
        listeners.computeIfAbsent(type, k -> new ArrayList<>())
                 .add((Consumer<Object>) listener);
    }

    public void post(Object event) {
        List<Consumer<Object>> handlers = listeners.get(event.getClass());
        if (handlers != null) handlers.forEach(h -> h.accept(event));
    }

    public <T> void unsubscribe(Class<T> type, Consumer<T> listener) {
        List<Consumer<Object>> handlers = listeners.get(type);
        if (handlers != null) handlers.remove(listener);
    }
}
