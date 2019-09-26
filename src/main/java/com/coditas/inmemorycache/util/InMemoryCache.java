package com.coditas.inmemorycache.util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Cache utility class, that uses LRU map to store cached objects
 * This class provides API to store and retrieve objects from cache
 * @param <K> object key
 * @param <T> object value
 */
public final class InMemoryCache<K, T> {

    /** cache map. */
    private ConcurrentHashMap cacheMap;

    /** inmemory cache instance. */
    private static InMemoryCache<String, Object> instance = null;

    private int max_items;

    public int getMax_items() {
        return max_items;
    }

    public class CacheObject implements Comparable{
        public T value;

        private Long timestamp = System.currentTimeMillis();

        protected CacheObject(T value) {
            this.value = value;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }

        public T getValue() {
            return value;
        }

        @Override
        public int compareTo(Object o) {
            CacheObject cacheObject = (CacheObject) o;
            return this.timestamp.compareTo(cacheObject.getTimestamp());
        }
    }

    private InMemoryCache(int maxItems) {
        cacheMap = new ConcurrentHashMap();
        this.max_items = maxItems;
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
        if(cacheMap.size() >= max_items) {
            List<ConcurrentHashMap.Entry> list = (List) cacheMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toList());
            if(list != null && !list.isEmpty()) {
                cacheMap.remove(list.get(0).getKey());
            }
        }
        cacheMap.put(key, new CacheObject(value));
    }

    /**
     * get object from cache.
     * @param key key
     * @return object from cache
     */
    public T get(K key) {
        CacheObject c = (CacheObject) cacheMap.get(key);

        if (c == null)
            return null;
        else {
            c.setTimestamp(System.currentTimeMillis());
            return c.value;
        }
    }

    /**
     *
     * @return returns size of cache
     */
    public int size() {
        return cacheMap.size();
    }

    /**
     * empties cache
     */
    public void flush() {
        cacheMap.clear();
    }

    /**
     * delete object from cache.
     * @param key key
     */
    public void delete(K key) {
        cacheMap.remove(key);
    }

    public ConcurrentHashMap getCacheMap() {
        return cacheMap;
    }
}
