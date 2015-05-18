package com.ankur.stackoverflow.cache;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FailSafeCacheManager implements CacheManager {

    private final ConcurrentMap<String, FailSafeCache> failSafeCaches = new ConcurrentHashMap<>();

    private CacheManager cacheManager;

    public FailSafeCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public Cache getCache(String name) {
        FailSafeCache failSafeCache = failSafeCaches.get(name);
        if (failSafeCache == null) {
            Cache targetCache = cacheManager.getCache(name);
            failSafeCache = getFailSafeCache(targetCache);
            failSafeCaches.put(name, failSafeCache);
        }
        return failSafeCache;
    }

    @Override
    public Collection<String> getCacheNames() {
        return cacheManager.getCacheNames();
    }

    protected FailSafeCache getFailSafeCache(Cache targetCache) {
        return new FailSafeCache(targetCache);
    }
}
