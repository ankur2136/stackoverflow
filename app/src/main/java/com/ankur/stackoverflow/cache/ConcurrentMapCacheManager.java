package com.ankur.stackoverflow.cache;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConcurrentMapCacheManager implements CacheManager {

    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>(16);

    private boolean dynamic = true;

    private boolean allowNullValues = true;

    /**
     * Construct a dynamic ConcurrentMapCacheManager,
     * lazily creating cache instances as they are being requested.
     */
    public ConcurrentMapCacheManager() {
    }

    /**
     * Construct a static ConcurrentMapCacheManager,
     * managing caches for the specified cache names only.
     */
    public ConcurrentMapCacheManager(String... cacheNames) {
        setCacheNames(Arrays.asList(cacheNames));
    }

    /**
     * Specify the set of cache names for this CacheManager's 'static' mode.
     * <p>The number of caches and their names will be fixed after a call to this method,
     * with no creation of further cache regions at runtime.
     * <p>Calling this with a {@code null} collection argument resets the
     * mode to 'dynamic', allowing for further creation of caches again.
     */
    public void setCacheNames(Collection<String> cacheNames) {
        if (cacheNames != null) {
            for (String name : cacheNames) {
                this.cacheMap.put(name, createConcurrentMapCache(name));
            }
            this.dynamic = false;
        } else {
            this.dynamic = true;
        }
    }

    /**
     * Specify whether to accept and convert {@code null} values for all caches
     * in this cache manager.
     * <p>Default is "true", despite ConcurrentHashMap itself not supporting {@code null}
     * values. An internal holder object will be used to store user-level {@code null}s.
     * <p>Note: A change of the null-value setting will reset all existing caches,
     * if any, to reconfigure them with the new null-value requirement.
     */
    public void setAllowNullValues(boolean allowNullValues) {
        if (allowNullValues != this.allowNullValues) {
            this.allowNullValues = allowNullValues;
            // Need to recreate all Cache instances with the new null-value configuration...
            for (Map.Entry<String, Cache> entry : this.cacheMap.entrySet()) {
                entry.setValue(createConcurrentMapCache(entry.getKey()));
            }
        }
    }

    /**
     * Return whether this cache manager accepts and converts {@code null} values
     * for all of its caches.
     */
    public boolean isAllowNullValues() {
        return this.allowNullValues;
    }


    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(this.cacheMap.keySet());
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = this.cacheMap.get(name);
        if (cache == null && this.dynamic) {
            synchronized (this.cacheMap) {
                cache = this.cacheMap.get(name);
                if (cache == null) {
                    cache = createConcurrentMapCache(name);
                    this.cacheMap.put(name, cache);
                }
            }
        }
        return cache;
    }

    /**
     * Create a new ConcurrentMapCache instance for the specified cache name.
     *
     * @param name the name of the cache
     * @return the ConcurrentMapCache (or a decorator thereof)
     */
    protected Cache createConcurrentMapCache(String name) {
        return new ConcurrentMapCache(name, isAllowNullValues());
    }
}