package ru.programstore.prostore.core;

public interface Cache {
    <K, V> V get(K key);

    void put(Object key, Object value);

    boolean putIfAbsent(Object key, Object value);

    boolean remove(Object key);

    boolean containsKey(Object key);

    Registration registerCacheEntryListener(EntryListener cacheEntryListener);
}
