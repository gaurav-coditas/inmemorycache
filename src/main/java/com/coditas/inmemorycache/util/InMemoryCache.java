package com.coditas.inmemorycache.util;

import org.apache.commons.collections.map.LRUMap;

/**
 * Cache utility class, that uses LRU map to store cached objects
 * This class provides API to store and retrieve objects from cache
 * @param <K> object key
 * @param <T> object value
 */
public final class InMemoryCache<K, T> {

    /** cache map. */
    private LRUMap cacheMap;

    /** inmemory cache instance. */
    private static InMemoryCache<String, Object> instance = null;

    protected class CacheObject {
        public T value;

        protected CacheObject(T value) {
            this.value = value;
        }
    }

    private InMemoryCache(int maxItems) {
        cacheMap = new LRUMap(maxItems);
    }

    /**
     *
     * @return returns instance of in memory cache
     */
    public static InMemoryCache getInstance() {
        if(instance == null) {
            instance = new InMemoryCache(100);
        }
        return instance;
    }

    /**
     * adds object to cache.
     * @param key key
     * @param value value
     */
    public void put(K key, T value) {
        synchronized (cacheMap) {
            cacheMap.put(key, new CacheObject(value));
        }
    }

    /**
     * get object from cache.
     * @param key key
     * @return object from cache
     */
    public T get(K key) {
        synchronized (cacheMap) {
            CacheObject c = (CacheObject) cacheMap.get(key);

            if (c == null)
                return null;
            else {
                return c.value;
            }
        }
    }

    /**
     *
     * @return returns size of cache
     */
    public int size() {
        synchronized (cacheMap) {
            return cacheMap.size();
        }
    }

    /**
     * empties cache
     */
    public void flush() {
        synchronized (cacheMap) {
            cacheMap.clear();
        }
    }

    /**
     * delete object from cache.
     * @param key key
     */
    public void delete(K key) {
        synchronized (cacheMap) {
            cacheMap.remove(key);
        }
    }
}
