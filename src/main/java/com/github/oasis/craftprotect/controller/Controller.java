package com.github.oasis.craftprotect.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

public abstract class Controller<K, V> {

    private final List<BiConsumer<K, V>> subscribers = new ArrayList<>();

    public abstract V get(K key);

    public abstract Stream<K> getKeys();

    public abstract Stream<V> getValues();

    public abstract Stream<Map.Entry<K, V>> getEntries();

    public void updateAll(BiConsumer<K, V> consumer) {
        getEntries().forEach(kvEntry -> {
            consumer.accept(kvEntry.getKey(), kvEntry.getValue());
            update(kvEntry.getKey(), kvEntry.getValue());
        });
    }

    public final void update(K key, Consumer<V> modifier) {
        V value = get(key);
        modifier.accept(value);
        update(key, value);
    }

    public final void update(K key, V value) {
        try {
            update0(key, value);
            this.subscribers.forEach(kvBiConsumer -> kvBiConsumer.accept(key, value));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void update0(K key, V value);

    public final void subscribe(BiConsumer<K, V> consumer) {
        this.subscribers.add(consumer);
    }

    @Override
    public String toString() {
        return "Controller{" +
                "subscribers=" + subscribers +
                '}';
    }
}
