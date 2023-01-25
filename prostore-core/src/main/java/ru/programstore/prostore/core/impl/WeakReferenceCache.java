package ru.programstore.prostore.core.impl;

import ru.programstore.prostore.core.Cache;
import ru.programstore.prostore.core.EntryListener;
import ru.programstore.prostore.core.Registration;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class WeakReferenceCache implements Cache {
    private final ConcurrentMap<Object, Entry> cache = new ConcurrentHashMap<>();
    private final ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
    private final Set<EntryListener> adapters = new CopyOnWriteArraySet<>();

    @Override
    public Registration registerCacheEntryListener(EntryListener entryListener) {
        adapters.add(entryListener);
        return () -> adapters.remove(entryListener);
    }

    @Override
    public <K, V> V get(K key) {
        purgeItems();
        final Reference<Object> entry = cache.get(key);

        final V returnValue = entry == null ? null : (V) entry.get();
        if (returnValue != null) {
            for (EntryListener adapter : adapters) {
                adapter.onEntryRead(key, returnValue);
            }
        }
        return returnValue;
    }

    @Override
    public void put(Object key, Object value) {
        if (value == null) {
            throw new IllegalArgumentException("Null values not supported");
        }

        purgeItems();
        if (cache.put(key, new Entry(key, value)) != null) {
            for (EntryListener adapter : adapters) {
                adapter.onEntryUpdated(key, value);
            }
        } else {
            for (EntryListener adapter : adapters) {
                adapter.onEntryCreated(key, value);
            }
        }
    }

    @Override
    public boolean putIfAbsent(Object key, Object value) {
        if (value == null) {
            throw new IllegalArgumentException("Null values not supported");
        }
        purgeItems();
        if (cache.putIfAbsent(key, new Entry(key, value)) == null) {
            for (EntryListener adapter : adapters) {
                adapter.onEntryCreated(key, value);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Object key) {
        if (cache.remove(key) != null) {
            for (EntryListener adapter : adapters) {
                adapter.onEntryRemoved(key);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        purgeItems();
        final Reference<Object> entry = cache.get(key);

        return entry != null && entry.get() != null;
    }

    private void purgeItems() {
        Entry purgedEntry;
        while ((purgedEntry = (Entry) referenceQueue.poll()) != null) {
            if (cache.remove(purgedEntry.getKey()) != null) {
                for (EntryListener adapter : adapters) {
                    adapter.onEntryExpired(purgedEntry.getKey());
                }
            }
        }
    }

    private class Entry extends WeakReference<Object> {
        private final Object key;

        public Entry(Object key, Object value) {
            super(value, referenceQueue);
            this.key = key;
        }

        public Object getKey() {
            return key;
        }
    }
}
